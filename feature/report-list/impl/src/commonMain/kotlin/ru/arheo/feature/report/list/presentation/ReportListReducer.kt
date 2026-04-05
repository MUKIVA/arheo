package ru.arheo.feature.report.list.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import ru.arheo.feature.report.list.domain.models.Report
import ru.arheo.feature.report.list.presentation.models.UiReport

internal class ReportListReducer : Reducer<ReportListStore.State, ReportListPatch> {
    override fun ReportListStore.State.reduce(msg: ReportListPatch): ReportListStore.State {
        return when (this) {
            is ReportListStore.State.Content -> reduce(this, msg)
            is ReportListStore.State.Loading -> reduce(this, msg)
        }
    }

    private fun reduce(
        state: ReportListStore.State.Loading,
        patch: ReportListPatch
    ) = when (patch) {
        is ReportListPatch.ReportsLoaded -> ReportListStore.State.Content(
            reports = patch.reports.asUiReports()
        )
        else -> state
    }


    private fun reduce(
        state: ReportListStore.State.Content,
        patch: ReportListPatch
    ): ReportListStore.State {
        return when (patch) {
            is ReportListPatch.Loading -> ReportListStore.State.Loading
            is ReportListPatch.ReportsLoaded -> state.copy(
                reports = patch.reports.asUiReports()
            )
            is ReportListPatch.SearchQueryChanged -> state.copy(
                searchQuery = patch.query,
            )
            is ReportListPatch.RequestDelete -> state.copy(
                deletingReportId = patch.reportId
            )
            is ReportListPatch.DismissDelete -> state.copy(
                deletingReportId = null
            )
            is ReportListPatch.ReportDeleted -> state.copy(
                deletingReportId = null,
                reports = patch.reports.asUiReports()
            )
        }
    }
}

private fun List<Report>.asUiReports(): List<UiReport> {
    return map { item ->
        UiReport(
            id = item.id,
            name = item.name,
            authors = item.authors,
            year = item.year,
            workType = item.workType
        )
    }
}