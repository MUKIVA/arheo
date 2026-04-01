package ru.arheo.feature.report.selector.ui

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import java.awt.datatransfer.DataFlavor
import java.io.File

internal class DefaultDragDropTarget(
    private val onDragOver: (Boolean) -> Unit,
    private val onFilesDropped: (List<String>) -> Unit
) : DragAndDropTarget {
    override fun onStarted(event: DragAndDropEvent) {
        onDragOver(true)
    }
    override fun onEnded(event: DragAndDropEvent) {
        onDragOver(false)
    }
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onDrop(event: DragAndDropEvent): Boolean {
        val transferable = event.awtTransferable
        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            @Suppress("UNCHECKED_CAST")
            val droppedFiles = transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
            val paths = droppedFiles.map { it.absolutePath }
            if (paths.isNotEmpty()) {
                onFilesDropped(paths)
                return true
            }
        }
        return false
    }
}