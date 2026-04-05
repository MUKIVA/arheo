package ru.arheo.feature.report.viewer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arheo.feature.report_viewer.impl.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import ru.arheo.feature.report.viewer.presentation.ReportViewerComponent
import ru.arheo.feature.report.viewer.presentation.ReportViewerStore
import ru.arheo.feature.report.viewer.presentation.models.UiMonument
import ru.arheo.feature.report.viewer.ui.defaults.ContentDefaults
import ru.arheo.feature.report.viewer.ui.models.MainInfoRow

private val containerModifier: Modifier
    @Composable get() = Modifier
        .fillMaxWidth()
        .background(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = MaterialTheme.shapes.large
        )
        .padding(16.dp)

private val containerStartModifier: Modifier
    @Composable get() = Modifier
        .fillMaxWidth()
        .background(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = MaterialTheme.shapes.large.copy(
                bottomStart = CornerSize(0.dp),
                bottomEnd = CornerSize(0.dp)
            )
        )
        .padding(16.dp)

private val containerEndModifier: Modifier
    @Composable get() = Modifier
        .fillMaxWidth()
        .background(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = MaterialTheme.shapes.large.copy(
                topStart = CornerSize(0.dp),
                topEnd = CornerSize(0.dp)
            )
        )
        .padding(16.dp)

private val containerMiddleModifier: Modifier
    @Composable get() = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceContainer)
        .padding(16.dp)

@Composable
internal fun Content(
    component: ReportViewerComponent,
    state: ReportViewerStore.State.Content,
    modifier: Modifier = Modifier,
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
) {
    Scaffold(
        topBar = { ReportViewerTopAppBar(state.report.name) },
        content = { paddingValues ->
            ReportViewContent(
                component = component,
                state = state,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    )
}

@Composable
private fun ReportViewContent(
    component: ReportViewerComponent,
    state: ReportViewerStore.State.Content,
    modifier: Modifier = Modifier,
) {
    val containerModifier = containerModifier

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val rows = ContentDefaults.buildMainInfo(state.report)

        item { Spacer(Modifier.size(16.dp)) }

        item {
            MainActions(
                state = state,
                component = component,
                modifier = containerModifier
            )
            Spacer(Modifier.size(16.dp))
        }

        item {
            MainInfoTable(
                rows = rows,
                modifier = containerModifier
            )
            Spacer(Modifier.size(16.dp))
        }

        monumentList(items = state.monuments)

        item { Spacer(Modifier.size(16.dp)) }
    }
}

private fun LazyListScope.monumentList(
    items: List<UiMonument>,
) = monumentListContent(list = items)

private fun LazyListScope.monumentListContent(
    list: List<UiMonument>,
) = itemsIndexed(
    items = list,
    key = { _, monument -> monument.key }
) { index, monument ->
    when (monument) {
        is UiMonument.Header if list.size == 1 ->
            MonumentListEmptyView(containerModifier)

        is UiMonument.Header -> {
            Text(
                text = stringResource(Res.string.monument_list_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = containerStartModifier
            )
            HorizontalDivider()
        }

        is UiMonument.Item if index == list.lastIndex -> MonumentListItem(
            monument = monument,
            modifier = containerEndModifier
        )

        is UiMonument.Item -> MonumentListItem(
            monument = monument,
            modifier = containerMiddleModifier
        )
    }
}


@Composable
private fun MonumentListItem(
    monument: UiMonument.Item,
    modifier: Modifier = Modifier
) = Column(modifier = modifier) {
    val monumentRows = ContentDefaults.buildMonumentInfo(monument)

    monumentRows.onEach { row ->
        MainInfoRow(
            label = stringResource(row.label),
            value = row.value
        )
    }

    HorizontalDivider(Modifier.padding(top = 16.dp))
}

@Composable
private fun MonumentListEmptyView(
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
) {
    Text(stringResource(Res.string.monument_list_empty_message))
}

@Composable
private fun MainActions(
    state: ReportViewerStore.State.Content,
    component: ReportViewerComponent,
    modifier: Modifier = Modifier
) = FlowRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    OutlinedButton(
        onClick = component::back,
        shape = MaterialTheme.shapes.large
    ) {
        Text(stringResource(Res.string.main_actions_back))
    }
    if (state.hasAttachedFiles) {
        OutlinedButton(
            onClick = component::openMaterials,
            shape = MaterialTheme.shapes.large
        ) {
            Text(stringResource(Res.string.main_actions_open_in_explorer))
        }
    }
}


@Composable
private fun MainInfoTable(
    rows: List<MainInfoRow>,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier,
) {
    Text(
        text = stringResource(Res.string.main_info_title),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    HorizontalDivider(Modifier.padding(bottom = 16.dp))
    rows.onEach { row ->
        MainInfoRow(
            label = stringResource(row.label),
            value = row.value,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MainInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
) {
    Text(
        text = label,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.weight(0.5f)
    )
    VerticalDivider()
    Text(value, Modifier.weight(0.5f))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportViewerTopAppBar(
    title: String,
    modifier: Modifier = Modifier
) = TopAppBar(
    title = { Text(title) },
    modifier = modifier
)