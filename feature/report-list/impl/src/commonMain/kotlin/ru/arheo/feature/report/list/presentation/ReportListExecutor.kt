package ru.arheo.feature.report.list.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.arheo.feature.report.list.domain.ReportRepository

@OptIn(FlowPreview::class)
internal class ReportListExecutor(
    private val repository: ReportRepository
) : CoroutineExecutor<
        ReportListStore.Intent,
        ReportListAction,
        ReportListStore.State,
        ReportListPatch,
        Nothing
        >() {

    private val searchQueryFlow = MutableStateFlow(String())

    init {
        searchQueryFlow
            .debounce(SEARCH_DEBOUNCE_MS)
            .onEach(::handleSearch)
            .launchIn(scope)
    }

    override fun executeAction(action: ReportListAction) {
        when (action) {
            is ReportListAction.ReportsLoaded ->
                dispatch(ReportListPatch.ReportsLoaded(action.reports))
        }
    }

    override fun executeIntent(intent: ReportListStore.Intent) {
        when (intent) {
            is ReportListStore.Intent.Refresh -> handleRefresh()
            is ReportListStore.Intent.UpdateSearchQuery -> scope.launch {
                searchQueryFlow.emit(intent.query)
                dispatch(ReportListPatch.SearchQueryChanged(intent.query))
            }
            is ReportListStore.Intent.RequestDeleteReport -> dispatch(ReportListPatch.RequestDelete(intent.reportId))
            is ReportListStore.Intent.ConfirmDeleteReport -> handleConfirmDelete()
            is ReportListStore.Intent.DismissDeleteReport -> dispatch(ReportListPatch.DismissDelete)
        }
    }

    private fun handleRefresh() {
        when (val instance = state()) {
            is ReportListStore.State.Content -> scope.launch {
                val reports = repository.searchReports(instance.searchQuery)
                dispatch(ReportListPatch.ReportsLoaded(reports))
            }
            ReportListStore.State.Loading -> Unit
        }
    }

    private fun handleSearch(query: String) {
        when (state()) {
            is ReportListStore.State.Content -> scope.launch {
                val reports = repository.searchReports(query)
                dispatch(ReportListPatch.ReportsLoaded(reports))
            }
            ReportListStore.State.Loading -> Unit
        }
    }

    private fun handleConfirmDelete() {
        when (val instance = state()) {
            is ReportListStore.State.Content -> scope.launch {
                val reportId = instance.deletingReportId ?: return@launch
                repository.deleteReport(reportId)
                val reports = repository.searchReports(instance.searchQuery)
                dispatch(ReportListPatch.ReportDeleted(reports))
            }
            ReportListStore.State.Loading -> Unit
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 1000L
    }
}