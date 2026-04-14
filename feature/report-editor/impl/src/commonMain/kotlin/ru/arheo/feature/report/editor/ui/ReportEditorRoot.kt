package ru.arheo.feature.report.editor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import ru.arheo.feature.report.editor.presentation.ReportEditorComponent
import ru.arheo.feature.report.editor.presentation.ReportEditorStore
import ru.arheo.feature.report.selector.ReportSelectorFeatureLauncher

@Composable
internal fun ReportEditorRoot(
    modifier: Modifier = Modifier,
    reportSelector: ReportSelectorFeatureLauncher = koinInject(),
    component: ReportEditorComponent = koinInject(),
) {
    val state by component.state.collectAsState()

    when (val instance = state) {
        is ReportEditorStore.State.Content -> ReportEditorContent(
            component = component,
            state = instance,
            reportSelector = reportSelector,
            modifier = modifier,
        )
        is ReportEditorStore.State.Loading ->
            ReportEditorLoading(modifier)
        is ReportEditorStore.State.Error ->
            ReportEditorError(modifier)
    }
}
