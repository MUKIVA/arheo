package ru.arheo.feature.report_list.presentation

import com.arkivanov.mvikotlin.core.store.Store
import ru.arheo.core.domain.model.Report

interface ReportListStore : Store<ReportListStore.Intent, ReportListStore.State, ReportListStore.Label> {

    sealed interface Intent {
        data object Refresh : Intent
        data class UpdateSearchQuery(val query: String) : Intent
        data class RequestDeleteReport(val reportId: Long) : Intent
        data object ConfirmDeleteReport : Intent
        data object DismissDeleteReport : Intent
    }

    data class State(
        val reports: List<Report> = emptyList(),
        val searchQuery: String = "",
        val isLoading: Boolean = true,
        val deletingReportId: Long? = null,
    )

    sealed interface Label {
        data class EditReport(val reportId: Long) : Label
        data object CreateReport : Label
    }
}
