package ru.arheo.feature.report_selector.presentation

import com.arkivanov.mvikotlin.core.store.Store
import ru.arheo.core.domain.model.FileInfo

internal interface ReportSelectorStore :
    Store<ReportSelectorStore.Intent, ReportSelectorStore.State, Nothing> {

    sealed interface Intent {
        data class AttachFiles(val paths: List<String>) : Intent
        data class RemoveFile(val fileName: String) : Intent
        data class UpdateDragOver(val isDragging: Boolean) : Intent
        data class LoadArchive(val archivePath: String) : Intent
    }

    data class State(
        val attachedFiles: List<FileInfo> = emptyList(),
        val isDraggingOver: Boolean = false,
        val workingDirectory: String = "",
        val isLoading: Boolean = false,
    )
}
