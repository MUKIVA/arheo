package ru.arheo.feature.report.selector.presentation

import kotlinx.coroutines.flow.StateFlow
import ru.arheo.feature.report.selector.presentation.models.UiChooseType
import java.awt.datatransfer.Transferable
import java.nio.file.Path

internal interface ReportSelectorComponent {

    val state: StateFlow<ReportSelectorStore.State>

    fun onAttachFiles(dialogTitle: String, type: UiChooseType)
    fun onDrop(transferable: Transferable): Boolean
    fun onRemoveFile(fileName: String)
    fun onDragOver(isDragging: Boolean)
    fun loadArchive(archive: Path)

}
