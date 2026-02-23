package ru.arheo.feature.report_list.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.arheo.core.data.ReportRepository
import ru.arheo.core.domain.model.Report

class ReportListStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ReportRepository,
) {

    fun create(): ReportListStore {
        return object : ReportListStore,
            Store<ReportListStore.Intent, ReportListStore.State, ReportListStore.Label> by storeFactory.create(
                name = "ReportListStore",
                initialState = ReportListStore.State(),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}
    }

    private sealed interface Action {
        data class ReportsLoaded(val reports: List<Report>) : Action
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class ReportsLoaded(val reports: List<Report>) : Msg
        data class SearchQueryChanged(val query: String, val filtered: List<Report>) : Msg
        data class RequestDelete(val reportId: Long) : Msg
        data object DismissDelete : Msg
        data class ReportDeleted(val reports: List<Report>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                val reports = repository.getAllReports()
                dispatch(Action.ReportsLoaded(reports))
            }
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ReportListStore.Intent, Action, ReportListStore.State, Msg, ReportListStore.Label>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.ReportsLoaded -> dispatch(Msg.ReportsLoaded(action.reports))
            }
        }

        override fun executeIntent(intent: ReportListStore.Intent) {
            when (intent) {
                is ReportListStore.Intent.UpdateSearchQuery -> handleSearch(intent.query)
                is ReportListStore.Intent.RequestDeleteReport -> dispatch(Msg.RequestDelete(intent.reportId))
                is ReportListStore.Intent.ConfirmDeleteReport -> handleConfirmDelete()
                is ReportListStore.Intent.DismissDeleteReport -> dispatch(Msg.DismissDelete)
            }
        }

        private fun handleSearch(query: String) {
            scope.launch {
                val filtered = repository.searchReports(query)
                dispatch(Msg.SearchQueryChanged(query, filtered))
            }
        }

        private fun handleConfirmDelete() {
            val reportId = state().deletingReportId ?: return
            scope.launch {
                repository.deleteReport(reportId)
                val reports = repository.searchReports(state().searchQuery)
                dispatch(Msg.ReportDeleted(reports))
            }
        }
    }

    private object ReducerImpl : Reducer<ReportListStore.State, Msg> {
        override fun ReportListStore.State.reduce(msg: Msg): ReportListStore.State =
            when (msg) {
                is Msg.Loading -> copy(isLoading = true)
                is Msg.ReportsLoaded -> copy(reports = msg.reports, isLoading = false)
                is Msg.SearchQueryChanged -> copy(searchQuery = msg.query, reports = msg.filtered)
                is Msg.RequestDelete -> copy(deletingReportId = msg.reportId)
                is Msg.DismissDelete -> copy(deletingReportId = null)
                is Msg.ReportDeleted -> copy(deletingReportId = null, reports = msg.reports)
            }
    }
}
