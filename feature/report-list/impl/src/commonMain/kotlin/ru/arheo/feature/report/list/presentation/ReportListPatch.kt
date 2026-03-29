package ru.arheo.feature.report.list.presentation

import ru.arheo.feature.report.list.domain.models.Report

internal sealed interface ReportListPatch {
    data object Loading : ReportListPatch
    data class ReportsLoaded(val reports: List<Report>) : ReportListPatch
    data class SearchQueryChanged(val query: String, val filtered: List<Report>) : ReportListPatch
    data class RequestDelete(val reportId: Long) : ReportListPatch
    data object DismissDelete : ReportListPatch
    data class ReportDeleted(val reports: List<Report>) : ReportListPatch
}