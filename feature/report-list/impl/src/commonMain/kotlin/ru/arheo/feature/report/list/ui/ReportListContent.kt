package ru.arheo.feature.report.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import ru.arheo.feature.report.list.presentation.ReportListComponent
import ru.arheo.feature.report.list.presentation.ReportListStore
import ru.arheo.feature.report.list.presentation.models.UiReport

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ReportListContent(
    state: ReportListStore.State.Content,
    component: ReportListComponent,
    modifier: Modifier = Modifier,
) = Scaffold(
    topBar = {
        OutlinedTextField(
            value = state.searchQuery,
            shape = MaterialTheme.shapes.large,
            onValueChange = component::onSearchQueryChanged,
            label = { Text(stringResource(Res.string.query_label)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    },
    content = { paddingValues ->
        ReportTable(
            reports = state.reports,
            component = component,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp)
                .clip(MaterialTheme.shapes.large.copy(
                    bottomEnd = CornerSize(0.dp),
                    bottomStart = CornerSize(0.dp)
                ))
                .background(MaterialTheme.colorScheme.surfaceContainer)
        )
        if (state.deletingReportId != null) {
            DeleteConfirmationDialog(
                onConfirm = component::onConfirmDeleteReport,
                onDismiss = component::onDismissDeleteReport,
            )
        }
    },
    modifier = modifier.padding(top = 16.dp)
)

@Composable
private fun ReportTable(
    reports: List<UiReport>,
    component: ReportListComponent,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier
) {
    TableActions(component, Modifier.padding(16.dp))
    HorizontalDivider()
    ReportTableHeader(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
    HorizontalDivider()
    when {
        reports.isEmpty() -> ReportListEmptyView(Modifier.fillMaxSize())
        else -> ReportList(
            reports = reports,
            component = component,
            modifier = Modifier.fillMaxSize(),
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
    component: ReportListComponent,
    modifier: Modifier = Modifier,
) = LazyColumn(modifier = modifier) {
    items(reports, key = { it.id }) { report ->
        ReportItem(
            report = report,
            onEditReport =   { component.onEditReport(report.id) },
            onDeleteReport = { component.onRequestDeleteReport(report.id) },
            onViewReport =   { component.onOpenReport(report.id) }
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
    onViewReport: () -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .clickable { onViewReport() }
        .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
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
            Button(
                onClick = onConfirm,
                shape = MaterialTheme.shapes.large,
            ) {
                Text(stringResource(Res.string.delete_alert_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = MaterialTheme.shapes.large,
            ) {
                Text(stringResource(Res.string.delete_alert_dismiss))
            }
        },
    )
}

@Composable
private fun TableActions(
    component: ReportListComponent,
    modifier: Modifier = Modifier
) = FlowRow(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    OutlinedButton(
        onClick = component::onCreateReport,
        shape = MaterialTheme.shapes.large
    ) {
        Text(stringResource(Res.string.action_add_report))
    }
}