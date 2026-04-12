@file:OptIn(ExperimentalComposeUiApi::class)

package ru.arheo.feature.report.selector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import arheo.feature.report_selector.impl.generated.resources.Res
import arheo.feature.report_selector.impl.generated.resources.selector_action_add_directory
import arheo.feature.report_selector.impl.generated.resources.selector_action_add_files
import arheo.feature.report_selector.impl.generated.resources.selector_action_remove
import arheo.feature.report_selector.impl.generated.resources.selector_attached_files_count
import arheo.feature.report_selector.impl.generated.resources.selector_directory_chooser_title
import arheo.feature.report_selector.impl.generated.resources.selector_drop_zone_active
import arheo.feature.report_selector.impl.generated.resources.selector_drop_zone_idle
import arheo.feature.report_selector.impl.generated.resources.selector_file_chooser_title
import arheo.feature.report_selector.impl.generated.resources.selector_file_size_bytes
import arheo.feature.report_selector.impl.generated.resources.selector_file_size_gb
import arheo.feature.report_selector.impl.generated.resources.selector_file_size_kb
import arheo.feature.report_selector.impl.generated.resources.selector_file_size_mb
import arheo.feature.report_selector.impl.generated.resources.selector_section_title
import org.jetbrains.compose.resources.stringResource
import ru.arheo.feature.report.selector.presentation.ReportSelectorComponent
import ru.arheo.feature.report.selector.presentation.ReportSelectorStore
import ru.arheo.feature.report.selector.presentation.models.UiChooseType
import ru.arheo.feature.report.selector.presentation.models.UiFileInfo
import java.awt.datatransfer.Transferable

@Composable
internal fun ReportSelectorContent(
    component: ReportSelectorComponent,
    state: ReportSelectorStore.State.Content,
    modifier: Modifier = Modifier,
) {
    val fileChooserTitle = stringResource(Res.string.selector_file_chooser_title)
    val directoryChooserTitle = stringResource(Res.string.selector_directory_chooser_title)
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(Res.string.selector_section_title),
            style = MaterialTheme.typography.titleMedium,
        )
        FileDropZone(
            isDraggingOver = state.isDraggingOver,
            onFilesDropped = component::onDrop,
            onDragOver = component::onDragOver,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = {
                    component.onAttachFiles(fileChooserTitle, UiChooseType.FILE)
                },
                shape = MaterialTheme.shapes.large
            ) {
                Text(stringResource(Res.string.selector_action_add_files))
            }
            OutlinedButton(
                onClick = {
                    component.onAttachFiles(directoryChooserTitle, UiChooseType.DIRECTORY)
                },
                shape = MaterialTheme.shapes.large
            ) {
                Text(stringResource(Res.string.selector_action_add_directory))
            }
        }
        if (state.attachedFiles.isNotEmpty()) {
            Text(
                text = stringResource(Res.string.selector_attached_files_count, state.attachedFiles.size),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            state.attachedFiles.forEach { file ->
                FileRow(
                    file = file,
                    onRemove = { component.onRemoveFile(file.name) }
                )
            }
        }
    }
}

@Composable
private fun FileDropZone(
    isDraggingOver: Boolean,
    onFilesDropped: (Transferable) -> Boolean,
    onDragOver: (Boolean) -> Unit,
) {
    val borderColor = when (isDraggingOver) {
        true -> MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.outlineVariant
    }

    val backgroundColor = when (isDraggingOver) {
        true -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        false -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }

    val dragAndDropTarget = remember {
        DefaultDragDropTarget(onDragOver, onFilesDropped)
    }

    val dropZoneText = when (isDraggingOver) {
        true -> stringResource(Res.string.selector_drop_zone_active)
        false -> stringResource(Res.string.selector_drop_zone_idle)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(MaterialTheme.shapes.large)
            .background(backgroundColor)
            .border(2.dp, borderColor)
            .dragAndDropTarget(
                shouldStartDragAndDrop = { true },
                target = dragAndDropTarget,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = dropZoneText,
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
    file: UiFileInfo,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clip(shape = MaterialTheme.shapes.large)
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
        TextButton(
            onClick = onRemove,
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                text = stringResource(Res.string.selector_action_remove),
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
private fun formatFileSize(bytes: Long): String = when {
    bytes < 1024L ->
        stringResource(Res.string.selector_file_size_bytes, bytes)
    bytes < 1024L * 1024L ->
        stringResource(Res.string.selector_file_size_kb, bytes / 1024L)
    bytes < 1024L * 1024L * 1024L ->
        stringResource(Res.string.selector_file_size_mb, "%.1f".format(bytes / (1024.0 * 1024.0)))
    else ->
        stringResource(Res.string.selector_file_size_gb, "%.1f".format(bytes / (1024.0 * 1024.0 * 1024.0)))
}
