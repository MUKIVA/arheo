package ru.arheo.feature.report.viewer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import ru.arheo.feature.report.viewer.presentation.ReportViewerComponent
import ru.arheo.feature.report.viewer.presentation.ReportViewerStore

@Composable
internal fun ReportViewerRoot(
    modifier: Modifier = Modifier,
    component: ReportViewerComponent = koinInject()
) {
    val state by component.state.collectAsState()

    when (val instance = state) {
        is ReportViewerStore.State.Content -> Content(
            component = component,
            state = instance,
            modifier = modifier
        )
        is ReportViewerStore.State.Error -> Error(
            modifier = modifier,
            onBack = component::back,
            onRefresh = component::refresh
        )
        is ReportViewerStore.State.Loading -> Loading(
            modifier = modifier
        )
    }

}