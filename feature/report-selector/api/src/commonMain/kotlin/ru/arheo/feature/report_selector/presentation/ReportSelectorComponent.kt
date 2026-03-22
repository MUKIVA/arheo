package ru.arheo.feature.report_selector.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.arheo.core.domain.model.FileInfo

interface ReportSelectorComponent {

    val state: StateFlow<State>

    fun onAttachFiles(paths: List<String>)
    fun onRemoveFile(fileName: String)
    fun onDragOver(isDragging: Boolean)
    fun loadArchive(archivePath: String)

    data class State(
        val attachedFiles: List<FileInfo> = emptyList(),
        val isDraggingOver: Boolean = false,
        val workingDirectory: String = "",
        val isLoading: Boolean = false,
    ) {
        val hasFiles: Boolean get() = attachedFiles.isNotEmpty()
    }

    fun interface Factory {
        fun create(componentContext: ComponentContext): ReportSelectorComponent
    }
}
