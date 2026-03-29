package ru.arheo.feature.report.list.ui

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import arheo.feature.report_list.impl.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ru.arheo.feature.report.list.presentation.ReportListStore
import ru.arheo.feature.report.list.presentation.models.UiReport

@Composable
internal fun ReportListContent(
    state: ReportListStore.State.Content,
    modifier: Modifier = Modifier,
    onSearchQueryChanged: (String) -> Unit = {},
    onAddReportClick: () -> Unit = {},
    onEditReportClick: (Long) -> Unit = {},
    onDeleteReportClick: (Long) -> Unit = {},
    onDeleteConfirm: () -> Unit = {},
    onDeleteDismiss: () -> Unit = {}
) = Column(modifier = modifier) {
    TopBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        searchQuery = state.searchQuery,
        onSearchQueryChanged = onSearchQueryChanged,
        onAddClick = onAddReportClick,
    )

    ReportTable(
        reports = state.reports,
        onEditReport = onEditReportClick,
        onDeleteReport = onDeleteReportClick,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                shape = RoundedCornerShape(16.dp)
            )
    )
    if (state.deletingReportId != null) {
        DeleteConfirmationDialog(
            onConfirm = onDeleteConfirm,
            onDismiss = onDeleteDismiss,
        )
    }
}

@Composable
private fun TopBar(
    searchQuery: String,
    modifier: Modifier = Modifier,
    onSearchQueryChanged: (String) -> Unit = {},
    onAddClick: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            label = { Text(stringResource(Res.string.query_label)) },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Button(onClick = onAddClick) {
            Text(stringResource(Res.string.action_add_report))
        }
    }
}

@Composable
private fun ReportTable(
    reports: List<UiReport>,
    modifier: Modifier = Modifier,
    onEditReport: (Long) -> Unit = {},
    onDeleteReport: (Long) -> Unit = {},
) = Column(modifier = modifier) {
    ReportTableHeader(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
    HorizontalDivider(thickness = 2.dp)

    when {
        reports.isEmpty() -> ReportListEmptyView(Modifier.fillMaxSize())
        else -> ReportList(
            reports = reports,
            modifier = Modifier.fillMaxSize(),
            onEditReport = onEditReport,
            onDeleteReport = onDeleteReport
        )
    }

}

@Composable
private fun ReportListEmptyView(
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
) {
    Text(
        text  = stringResource(Res.string.list_empty_meassage),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun ReportList(
    reports: List<UiReport>,
    modifier: Modifier = Modifier,
    onEditReport: (Long) -> Unit,
    onDeleteReport: (Long) -> Unit
) = LazyColumn(modifier = modifier) {
    items(reports, key = { it.id }) { report ->
        ReportItem(
            report = report,
            onEditReport = { onEditReport(report.id) },
            onDeleteReport = { onDeleteReport(report.id) },
        )
        HorizontalDivider()
    }
}
@Composable
private fun ReportTableHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.table_title_report_name),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(3f),
        )
        Text(
            text = stringResource(Res.string.table_title_report_authors),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(2f),
        )
        Text(
            text = stringResource(Res.string.table_title_year),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.7f),
        )
        Text(
            text = stringResource(Res.string.table_title_work_type),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1.3f),
        )
        Text(
            text = stringResource(Res.string.table_title_actions),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(120.dp),
        )
    }
}

@Composable
private fun ReportItem(
    report: UiReport,
    onEditReport: () -> Unit,
    onDeleteReport: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = report.name,
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
            text = "${report.year}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.7f),
        )
        Text(
            text = report.workType,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1.3f),
        )
        ReportItemActions(
            modifier = Modifier.width(120.dp),
            onEditReport = onEditReport,
            onDeleteReport = onDeleteReport
        )
    }
}

@Composable
private fun ReportItemActions(
    modifier: Modifier = Modifier,
    onEditReport: () -> Unit = {},
    onDeleteReport: () -> Unit = {}
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.End
) {
    IconButton(onClick = onEditReport) {
        Icon(
            painter = painterResource(Res.drawable.ic_edit),
            contentDescription = stringResource(Res.string.item_action_edit_content_description)
        )
    }
    IconButton(onClick = onDeleteReport) {
        Icon(
            painter = painterResource(Res.drawable.ic_delete),
            contentDescription = stringResource(Res.string.item_action_delete_content_description)
        )
    }
}


@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.delete_alert_title)) },
        text = { Text(stringResource(Res.string.delete_alert_description)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(Res.string.delete_alert_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.delete_alert_dismiss))
            }
        },
    )
}
