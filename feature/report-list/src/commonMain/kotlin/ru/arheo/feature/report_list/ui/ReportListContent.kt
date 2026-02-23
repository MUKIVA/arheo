package ru.arheo.feature.report_list.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.arheo.core.domain.model.Report
import ru.arheo.feature.report_list.presentation.ReportListComponent

@Composable
fun ReportListContent(component: ReportListComponent) {
    val state by component.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            searchQuery = state.searchQuery,
            onSearchQueryChanged = component::onSearchQueryChanged,
            onAddClick = component::onCreateReport,
        )
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            ReportTable(
                reports = state.reports,
                onEdit = component::onEditReport,
                onDelete = component::onRequestDeleteReport,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .fillMaxSize(),
            )
        }
    }
    if (state.deletingReportId != null) {
        DeleteConfirmationDialog(
            onConfirm = component::onConfirmDeleteReport,
            onDismiss = component::onDismissDeleteReport,
        )
    }
}

@Composable
private fun TopBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onAddClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            label = { Text("Поиск по отчётам...") },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = onAddClick) {
            Text("Добавить отчёт")
        }
    }
}

@Composable
private fun ReportTable(
    reports: List<Report>,
    onEdit: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ReportTableHeader()
        HorizontalDivider(thickness = 2.dp)
        if (reports.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Отчёты не найдены", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(reports, key = { it.id }) { report ->
                    ReportTableRow(
                        report = report,
                        onEdit = { onEdit(report.id) },
                        onDelete = { onDelete(report.id) },
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun ReportTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Название отчёта",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(3f),
        )
        Text(
            text = "Автор(ы)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2f),
        )
        Text(
            text = "Год",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.7f),
        )
        Text(
            text = "Тип работ",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.3f),
        )
        Text(
            text = "Действия",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp),
        )
    }
}

@Composable
private fun ReportTableRow(
    report: Report,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = report.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(3f),
        )
        Text(
            text = report.authors.joinToString(", "),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(2f),
        )
        Text(
            text = report.year.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.7f),
        )
        Text(
            text = report.workType,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1.3f),
        )
        Row(
            modifier = Modifier.width(120.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(onClick = onEdit) {
                Text("Изм.", style = MaterialTheme.typography.labelSmall)
            }
            TextButton(onClick = onDelete) {
                Text("Удл.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Удаление отчёта") },
        text = { Text("Вы уверены, что хотите удалить этот отчёт? Это действие нельзя отменить.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Удалить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
    )
}
