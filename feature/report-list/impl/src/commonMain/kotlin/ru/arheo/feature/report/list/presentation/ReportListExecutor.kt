package ru.arheo.feature.report.list.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.arheo.feature.report.list.domain.ReportRepository

internal class ReportListExecutor(
    private val repository: ReportRepository
) : CoroutineExecutor<
        ReportListStore.Intent,
        ReportListAction,
        ReportListStore.State,
        ReportListPatch,
        ReportListStore.Label
        >() {

    override fun executeAction(action: ReportListAction) {
        when (action) {
            is ReportListAction.ReportsLoaded ->
                dispatch(ReportListPatch.ReportsLoaded(action.reports))
        }
    }

    override fun executeIntent(intent: ReportListStore.Intent) {
        when (intent) {
            is ReportListStore.Intent.Refresh -> handleRefresh()
            is ReportListStore.Intent.UpdateSearchQuery -> handleSearch(intent.query)
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
                val filtered = repository.searchReports(query)
                dispatch(ReportListPatch.SearchQueryChanged(query, filtered))
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
}