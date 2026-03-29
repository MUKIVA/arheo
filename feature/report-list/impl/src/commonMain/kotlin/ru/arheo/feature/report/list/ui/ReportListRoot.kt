package ru.arheo.feature.report.list.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import ru.arheo.feature.report.list.presentation.ReportListComponent
import ru.arheo.feature.report.list.presentation.ReportListStore

@Composable
internal fun ReportListRoot(
    modifier: Modifier = Modifier,
    component: ReportListComponent = koinInject()
) {
    val state by component.state.collectAsState()

    when (val instance = state) {
        ReportListStore.State.Loading -> ReportListLoading(modifier)
        is ReportListStore.State.Content -> ReportListContent(
            state = instance,
            modifier = modifier,
            onSearchQueryChanged = component::onSearchQueryChanged,
            onAddReportClick     = component::onCreateReport,
            onEditReportClick    = component::onEditReport,
            onDeleteReportClick  = component::onRequestDeleteReport,
            onDeleteConfirm      = component::onConfirmDeleteReport,
            onDeleteDismiss      = component::onDismissDeleteReport
        )
    }
}


