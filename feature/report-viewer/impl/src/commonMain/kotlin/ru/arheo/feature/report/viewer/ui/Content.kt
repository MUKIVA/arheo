package ru.arheo.feature.report.viewer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.arheo.feature.report.viewer.presentation.ReportViewerComponent
import ru.arheo.feature.report.viewer.presentation.ReportViewerStore

@Composable
internal fun Content(
    component: ReportViewerComponent,
    state: ReportViewerStore.State.Content,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
) {
    Text(text = "Report ${state.reportId} has been opened")
}