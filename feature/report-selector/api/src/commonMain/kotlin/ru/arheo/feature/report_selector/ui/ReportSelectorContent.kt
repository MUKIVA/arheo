@file:OptIn(ExperimentalComposeUiApi::class)

package ru.arheo.feature.report_selector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.arheo.core.domain.model.FileInfo
import ru.arheo.feature.report_selector.presentation.ReportSelectorComponent
import java.awt.datatransfer.DataFlavor
import java.io.File
import javax.swing.JFileChooser

@Composable
fun ReportSelectorContent(component: ReportSelectorComponent) {
    val state by component.state.collectAsState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Материалы", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().height(80.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            FileDropZone(
                isDraggingOver = state.isDraggingOver,
                onFilesDropped = component::onAttachFiles,
                onDragOver = component::onDragOver,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { pickFiles()?.let(component::onAttachFiles) }) {
                Text("Добавить файлы")
            }
            OutlinedButton(onClick = { pickDirectory()?.let { component.onAttachFiles(listOf(it)) } }) {
                Text("Добавить директорию")
            }
        }
        if (state.attachedFiles.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Прикреплённые файлы (${state.attachedFiles.size}):",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            state.attachedFiles.forEach { file ->
                FileRow(file = file, onRemove = { component.onRemoveFile(file.name) })
            }
        }
    }
}

@Composable
private fun FileDropZone(
    isDraggingOver: Boolean,
    onFilesDropped: (List<String>) -> Unit,
    onDragOver: (Boolean) -> Unit,
) {
    val borderColor = if (isDraggingOver) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }
    val backgroundColor = if (isDraggingOver) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }
    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onStarted(event: DragAndDropEvent) {
                onDragOver(true)
            }
            override fun onEnded(event: DragAndDropEvent) {
                onDragOver(false)
            }
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
    }
    val dashPathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    cornerRadius = CornerRadius(8.dp.toPx()),
                    style = Stroke(width = 2.dp.toPx(), pathEffect = dashPathEffect),
                )
            }
            .dragAndDropTarget(
                shouldStartDragAndDrop = { true },
                target = dragAndDropTarget,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (isDraggingOver) "Отпустите для добавления" else "Перетащите файлы сюда",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDraggingOver) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
    }
}

@Composable
private fun FileRow(
    file: FileInfo,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = if (file.isDirectory) "\uD83D\uDCC1" else "\uD83D\uDCC4",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = file.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = formatFileSize(file.size),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        TextButton(onClick = onRemove) {
            Text("Удалить", color = MaterialTheme.colorScheme.error)
        }
    }
}

private fun pickFiles(): List<String>? {
    val chooser = JFileChooser().apply {
        isMultiSelectionEnabled = true
        fileSelectionMode = JFileChooser.FILES_ONLY
        dialogTitle = "Выберите файлы"
    }
    return if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        chooser.selectedFiles.map { it.absolutePath }
    } else null
}

private fun pickDirectory(): String? {
    val chooser = JFileChooser().apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        dialogTitle = "Выберите директорию"
    }
    return if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        chooser.selectedFile.absolutePath
    } else null
}

private fun formatFileSize(bytes: Long): String = when {
    bytes < 1024L -> "$bytes Б"
    bytes < 1024L * 1024L -> "${bytes / 1024L} КБ"
    bytes < 1024L * 1024L * 1024L -> "${"%.1f".format(bytes / (1024.0 * 1024.0))} МБ"
    else -> "${"%.1f".format(bytes / (1024.0 * 1024.0 * 1024.0))} ГБ"
}
