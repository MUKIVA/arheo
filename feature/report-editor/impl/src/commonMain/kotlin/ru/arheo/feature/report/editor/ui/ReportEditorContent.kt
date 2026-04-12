package ru.arheo.feature.report.editor.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import arheo.feature.report_editor.impl.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import ru.arheo.feature.report.editor.presentation.ReportEditorComponent
import ru.arheo.feature.report.editor.presentation.ReportEditorStore
import ru.arheo.feature.report.editor.presentation.models.UiMonument
import ru.arheo.feature.report.editor.ui.component.ChipInputField
import ru.arheo.feature.report.editor.ui.component.MonumentInputField
import ru.arheo.feature.report.editor.ui.models.DynamicFieldState
import ru.arheo.feature.report.editor.ui.static.DynamicFieldDefaults
import ru.arheo.feature.report.selector.ReportSelectorFeatureLauncher
import java.time.Year

private val YEAR_RANGE: List<String> by lazy {
    (Year.now().value downTo 1950).map { it.toString() }
}

private val containerModifier: Modifier
    @Composable get() = Modifier
        .fillMaxWidth()
        .background(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = MaterialTheme.shapes.large
        )
        .padding(16.dp)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun ReportEditorContent(
    component: ReportEditorComponent,
    state: ReportEditorStore.State.Content,
    reportSelector: ReportSelectorFeatureLauncher,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) = Scaffold(
    modifier = modifier,
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = { TopAppBar(title = { EditorTitle(state.isEditing) }) },
    content = { paddingValues ->
        val dynamicFields = DynamicFieldDefaults.rememberDynamicFields(component, state)

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = paddingValues.calculateTopPadding())
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionButtons(
                isSaving = state.isSaving,
                component = component,
                modifier = containerModifier
            )
            StaticFields(
                state = state,
                onReportNameChanged = component::onNameChanged,
                onReportYearChanged = component::onYearChanged,
                onReportWorkTypeChanged = component::onWorkTypeChanged,
                modifier = containerModifier,
            )
            DynamicFields(
                fieldStateList = dynamicFields,
                modifier = containerModifier,
            )
            MonumentsSection(
                items = state.monuments,
                onItemUpdate = component::onUpdateMonument,
                onItemRemove = component::onRemoveMonument,
                onAddMonument = component::onAddMonument,
                modifier = containerModifier
            )
            reportSelector.launch(
                componentContext = component,
                modifier = containerModifier.fillMaxWidth(),
                archive = state.archive,
                working = state.woking
            )
            Spacer(Modifier.fillMaxWidth().height(paddingValues.calculateBottomPadding()))
        }
    }
)


@Composable
private fun EditorTitle(isEditing: Boolean) {
    Text(
        text = when {
            isEditing -> stringResource(Res.string.editor_title_edit)
            else      -> stringResource(Res.string.editor_title_create)
        }
    )
}

@Composable
private fun DynamicFields(
    fieldStateList: List<DynamicFieldState>,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    fieldStateList.onEach { field ->
        ChipInputField(
            label = stringResource(field.labelRes),
            placeholder = stringResource(field.placeholderRes),
            emptyMessage = stringResource(field.emptyMessageRes),
            chips = field.data.value,
            onAdd = field.actions::onAdd,
            onRemove = field.actions::onRemove
        )
    }
}

@Composable
private fun StaticFields(
    state: ReportEditorStore.State.Content,
    modifier: Modifier = Modifier,
    onReportNameChanged: (String) -> Unit,
    onReportYearChanged: (String) -> Unit,
    onReportWorkTypeChanged: (String) -> Unit
) = Column(modifier) {
    OutlinedTextField(
        value = state.name,
        shape = MaterialTheme.shapes.large,
        onValueChange = onReportNameChanged,
        label = { Text(stringResource(Res.string.editor_field_name_placeholder)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        YearDropdown(
            selectedYear = state.year,
            onYearSelected = onReportYearChanged,
            modifier = Modifier.width(180.dp),
        )
        OutlinedTextField(
            value = state.workType,
            shape = MaterialTheme.shapes.large,
            onValueChange = onReportWorkTypeChanged,
            label = { Text(stringResource(Res.string.editor_field_work_type_placeholder)) },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearDropdown(
    selectedYear: String,
    onYearSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isExpanded = remember { mutableStateOf(false) }
    var filterText by remember(selectedYear) { mutableStateOf(selectedYear) }
    val filteredYears = remember(filterText) {
        if (filterText.isBlank()) YEAR_RANGE
        else YEAR_RANGE.filter { it.contains(filterText) }
    }
    ExposedDropdownMenuBox(
        expanded = isExpanded.value,
        onExpandedChange = { isExpanded.value = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = filterText,
            shape = MaterialTheme.shapes.large,
            onValueChange = { value ->
                filterText = value.filter { it.isDigit() }
                if (!isExpanded.value) isExpanded.value = true
            },
            label = { Text(stringResource(Res.string.editor_field_year_placeholder)) },
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded.value) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = {
                isExpanded.value = false
                filterText = selectedYear
            },
        ) {
            filteredYears.forEach { year ->
                DropdownMenuItem(
                    text = { Text(year) },
                    onClick = {
                        onYearSelected(year)
                        filterText = year
                        isExpanded.value = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
private fun MonumentsSection(
    items: List<UiMonument>,
    onItemUpdate: (Int, UiMonument) -> Unit,
    onItemRemove: (Int) -> Unit,
    onAddMonument: () -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    Text(
        text = stringResource(Res.string.editor_section_monument_title),
        style = MaterialTheme.typography.titleMedium,
    )
    items.forEachIndexed { index, monument ->
        MonumentInputField(
            monument = monument,
            onUpdate = { item -> onItemUpdate(index, item) },
            onRemove = { onItemRemove(index) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
    OutlinedButton(
        onClick = onAddMonument,
        shape = MaterialTheme.shapes.large,
    ) {
        Text(stringResource(Res.string.editor_monument_action_add))
    }
}

@Composable
private fun ActionButtons(
    isSaving: Boolean,
    component: ReportEditorComponent,
    modifier: Modifier = Modifier,
) = FlowRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    Button(
        onClick = component::onSave,
        shape = MaterialTheme.shapes.large,
        enabled = !isSaving,
    ) {
        Text(
            if (isSaving) stringResource(Res.string.editor_action_saving)
            else          stringResource(Res.string.editor_action_save)
        )
    }
    OutlinedButton(
        onClick = component::onBack,
        shape = MaterialTheme.shapes.large,
    ) {
        Text(stringResource(Res.string.editor_action_cancel))
    }
}
