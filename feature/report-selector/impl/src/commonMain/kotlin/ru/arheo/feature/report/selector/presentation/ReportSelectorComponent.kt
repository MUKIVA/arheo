package ru.arheo.feature.report.selector.presentation

import kotlinx.coroutines.flow.StateFlow
import ru.arheo.core.domain.model.FileInfoData

interface ReportSelectorComponent {

//    val state: StateFlow<State>

    fun onAttachFiles(paths: List<String>)
    fun onRemoveFile(fileName: String)
    fun onDragOver(isDragging: Boolean)
    fun loadArchive(archivePath: String)

    data class State(
        val attachedFiles: List<FileInfoData> = emptyList(),
        val isDraggingOver: Boolean = false,
        val workingDirectory: String = "",
        val isLoading: Boolean = false,
    ) {
        val hasFiles: Boolean get() = attachedFiles.isNotEmpty()
    }

}