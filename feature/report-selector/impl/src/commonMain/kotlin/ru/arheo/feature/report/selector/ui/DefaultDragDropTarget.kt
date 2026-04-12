package ru.arheo.feature.report.selector.ui

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import java.awt.datatransfer.Transferable

internal class DefaultDragDropTarget(
    private val onDragOver: (Boolean) -> Unit,
    private val onFilesDropped: (Transferable) -> Boolean
) : DragAndDropTarget {

    override fun onStarted(event: DragAndDropEvent) {
        onDragOver(true)
    }

    override fun onEnded(event: DragAndDropEvent) {
        onDragOver(false)
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onDrop(event: DragAndDropEvent): Boolean {
        onFilesDropped(event.awtTransferable); return true
    }

}