package ru.arheo.feature.report.selector.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.mvikotlin.core.store.Store
import ru.arheo.feature.report.selector.presentation.models.UiFileInfo
import java.nio.file.Path

internal class ReportSelectorStore(
    implementation: Store<Intent, State, Nothing>
)
    : Store<ReportSelectorStore.Intent, ReportSelectorStore.State, Nothing>
    by implementation {

    sealed interface Intent {
        data class AttachFiles(val paths: List<Path>) : Intent
        data class RemoveFile(val fileName: String) : Intent
        data class UpdateDragOver(val isDragging: Boolean) : Intent
        data class LoadArchive(val archivePath: Path) : Intent
    }

    @Stable
    sealed interface State {
        @Immutable
        data object Loading : State

        @Immutable
        data class Content(
            val attachedFiles: List<UiFileInfo> = emptyList(),
            val isDraggingOver: Boolean = false
        ) : State
    }

}
