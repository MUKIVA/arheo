package ru.arheo.feature.report_list.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface ReportListComponent {

    val state: StateFlow<ReportListStore.State>

    fun onSearchQueryChanged(query: String)
    fun onEditReport(reportId: Long)
    fun onCreateReport()
    fun onRequestDeleteReport(reportId: Long)
    fun onConfirmDeleteReport()
    fun onDismissDeleteReport()

    sealed interface Output {
        data class EditReport(val reportId: Long) : Output
        data object CreateReport : Output
    }

    fun interface Factory {
        fun create(
            componentContext: ComponentContext,
            output: (Output) -> Unit,
        ): ReportListComponent
    }
}
