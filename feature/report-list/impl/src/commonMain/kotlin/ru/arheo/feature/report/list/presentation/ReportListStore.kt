package ru.arheo.feature.report.list.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.mvikotlin.core.store.Store
import ru.arheo.feature.report.list.presentation.models.UiReport

internal class ReportListStore(
    private val implementation: Store<Intent, State, Label>
)
    : Store<ReportListStore.Intent, ReportListStore.State, ReportListStore.Label>
    by implementation {

    sealed interface Intent {
        data object Refresh : Intent
        data class UpdateSearchQuery(val query: String) : Intent
        data class RequestDeleteReport(val reportId: Long) : Intent
        data object ConfirmDeleteReport : Intent
        data object DismissDeleteReport : Intent
    }

    @Stable
    sealed interface State {
        @Immutable
        data object Loading : State

        @Immutable
        data class Content(
            val reports: List<UiReport> = emptyList(),
            val searchQuery: String = String(),
            val deletingReportId: Long? = null
        ) : State
    }

    sealed interface Label {
        data class EditReport(val reportId: Long) : Label
        data object CreateReport : Label
    }
}