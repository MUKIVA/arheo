package ru.arheo.feature.report.viewer.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.mvikotlin.core.store.Store
import ru.arheo.feature.report.viewer.presentation.models.UiMonument
import ru.arheo.feature.report.viewer.presentation.models.UiReport

internal class ReportViewerStore(
    private val implementation: Store<Intent, State, Label>
) : Store<ReportViewerStore.Intent, ReportViewerStore.State, ReportViewerStore.Label> by implementation {
    sealed interface Intent {
        data object Refresh : Intent
        data object Loading : Intent
        data object OpenMaterials : Intent
    }

    @Stable
    sealed interface State {
        @Immutable
        data object Loading : State

        @Immutable
        data object Error : State

        @Immutable
        data class Content(
            val report: UiReport,
            val monuments: List<UiMonument>,
            val hasAttachedFiles: Boolean
        ) : State
    }

    sealed interface Label

}