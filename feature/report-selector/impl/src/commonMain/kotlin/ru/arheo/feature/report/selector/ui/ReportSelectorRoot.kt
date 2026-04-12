package ru.arheo.feature.report.selector.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject
import ru.arheo.feature.report.selector.presentation.ReportSelectorComponent
import ru.arheo.feature.report.selector.presentation.ReportSelectorStore

@Composable
internal fun ReportSelectorRoot(
    modifier: Modifier = Modifier,
    component: ReportSelectorComponent = koinInject(),
) {
    val state by component.state.collectAsState()
    when (val instance = state) {
        is ReportSelectorStore.State.Loading ->
            ReportSelectorLoading(modifier)
        is ReportSelectorStore.State.Content ->
            ReportSelectorContent(
                component = component,
                state     = instance,
                modifier  = modifier
            )
    }
}
