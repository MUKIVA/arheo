package ru.arheo.feature.report.selector.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.mvikotlin.core.store.Store
import ru.arheo.core.domain.model.FileInfoData

internal class ReportSelectorStore(
    implementation: Store<Intent, State, Nothing>
)
    : Store<ReportSelectorStore.Intent, ReportSelectorStore.State, Nothing>
    by implementation {

    sealed interface Intent {
        data class AttachFiles(val paths: List<String>) : Intent
        data class RemoveFile(val fileName: String) : Intent
        data class UpdateDragOver(val isDragging: Boolean) : Intent
        data class LoadArchive(val archivePath: String) : Intent
    }

    @Stable
    sealed interface State {
        @Immutable
        data object Loading : State

        @Immutable
        data class Content(
            val attachedFiles: List<FileInfoData> = emptyList(),
            val isDraggingOver: Boolean = false,
            val workingDirectory: String = String()
        ) : State
    }

}
