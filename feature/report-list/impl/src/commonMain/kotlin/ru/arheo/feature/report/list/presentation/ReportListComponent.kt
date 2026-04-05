package ru.arheo.feature.report.list.presentation

import kotlinx.coroutines.flow.StateFlow

internal interface ReportListComponent {

    val state: StateFlow<ReportListStore.State>

    fun onSearchQueryChanged(query: String)
    fun onEditReport(reportId: Long)
    fun onCreateReport()
    fun onRequestDeleteReport(reportId: Long)
    fun onConfirmDeleteReport()
    fun onDismissDeleteReport()
    fun onOpenReport(reportId: Long)

}