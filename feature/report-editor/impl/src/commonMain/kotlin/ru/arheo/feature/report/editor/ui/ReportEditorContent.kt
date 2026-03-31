package ru.arheo.feature.report.editor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import arheo.feature.report_editor.impl.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import ru.arheo.feature.report.editor.domian.models.monument.MonumentCulture
import ru.arheo.feature.report.editor.domian.models.monument.MonumentLocation
import ru.arheo.feature.report.editor.domian.models.monument.MonumentName
import ru.arheo.feature.report.editor.domian.models.monument.MonumentNumber
import ru.arheo.feature.report.editor.domian.models.monument.MonumentPeriod
import ru.arheo.feature.report.editor.domian.models.monument.MonumentType
import ru.arheo.feature.report.editor.presentation.ReportEditorStore
import ru.arheo.feature.report.editor.presentation.models.UiMonument
import java.time.Year

private val YEAR_RANGE: List<String> by lazy {
    (Year.now().value downTo 1950).map { it.toString() }
}

@Composable
internal fun ReportEditorContent(
    state: ReportEditorStore.State.Content,
    reportSelector: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onReportNameChanged: (String) -> Unit = {},
    onReportYearChanged: (String) -> Unit = {},
    onReportWorkTypeChanged: (String) -> Unit = {},
    onAddAuthor: (String) -> Unit = {},
    onRemoveAuthor: (String) -> Unit = {},
    onAddDistrict: (String) -> Unit = {},
    onRemoveDistrict: (String) -> Unit = {},
    onAddKeyword: (String) -> Unit = {},
    onRemoveKeyword: (String) -> Unit = {},
    onMonumentItemUpdate: (Int, UiMonument) -> Unit = { _, _ -> },
    onMonumentItemRemove: (Int) -> Unit = {},
    onMonumentItemAdd: () -> Unit = {},
    onSaveReport: () -> Unit = {},
    onCancel: () -> Unit = {}
) = Column(
    modifier = modifier
) {
    Text(
        text = when {
            state.isEditing -> stringResource(Res.string.editor_title_edit)
            else ->            stringResource(Res.string.editor_title_create)
        },
        style = MaterialTheme.typography.headlineMedium,
    )
    Spacer(modifier = Modifier.height(16.dp))
    if (state.error != null) {
        Text(
            text = state.error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
    MetadataSection(
        state = state,
        onReportNameChanged = onReportNameChanged,
        onReportYearChanged = onReportYearChanged,
        onReportWorkTypeChanged = onReportWorkTypeChanged,
        onAddAuthor = onAddAuthor,
        onRemoveAuthor = onRemoveAuthor,
        onAddDistrict = onAddDistrict,
        onRemoveDistrict = onRemoveDistrict,
        onAddKeyword = onAddKeyword,
        onRemoveKeyword = onRemoveKeyword,
    )
    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(8.dp))
    MonumentsSection(
        items = state.monuments,
        onItemUpdate = onMonumentItemUpdate,
        onItemRemove = onMonumentItemRemove,
        onAddMonument = onMonumentItemAdd,
    )
    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(8.dp))
    reportSelector()
    Spacer(modifier = Modifier.height(8.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(8.dp))
    ActionButtons(
        isSaving = state.isSaving,
        onSave = onSaveReport,
        onCancel = onCancel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MetadataSection(
    state: ReportEditorStore.State.Content,
    onReportNameChanged: (String) -> Unit,
    onReportYearChanged: (String) -> Unit,
    onReportWorkTypeChanged: (String) -> Unit,
    onAddAuthor: (String) -> Unit,
    onRemoveAuthor: (String) -> Unit,
    onAddDistrict: (String) -> Unit,
    onRemoveDistrict: (String) -> Unit,
    onAddKeyword: (String) -> Unit,
    onRemoveKeyword: (String) -> Unit,
) {
    OutlinedTextField(
        value = state.name,
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
            onValueChange = onReportWorkTypeChanged,
            label = { Text(stringResource(Res.string.editor_field_work_type_placeholder)) },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    ChipInputField(
        label = stringResource(Res.string.editor_field_authors_label),
        placeholder = stringResource(Res.string.editor_field_authors_input_placeholder),
        chips = state.authors,
        onAdd = onAddAuthor,
        onRemove = onRemoveAuthor,
    )
    Spacer(modifier = Modifier.height(8.dp))
    ChipInputField(
        label = stringResource(Res.string.editor_field_districts_label),
        placeholder = stringResource(Res.string.editor_field_districts_input_placeholder),
        chips = state.districts,
        onAdd = onAddDistrict,
        onRemove = onRemoveDistrict,
    )
    Spacer(modifier = Modifier.height(8.dp))
    ChipInputField(
        label = stringResource(Res.string.editor_field_keywords_label),
        placeholder = stringResource(Res.string.editor_field_keywords_input_placeholder),
        chips = state.keywords,
        onAdd = onAddKeyword,
        onRemove = onRemoveKeyword,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearDropdown(
    selectedYear: String,
    onYearSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var filterText by remember(selectedYear) { mutableStateOf(selectedYear) }
    val filteredYears = remember(filterText) {
        if (filterText.isBlank()) YEAR_RANGE
        else YEAR_RANGE.filter { it.contains(filterText) }
    }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = filterText,
            onValueChange = { value ->
                filterText = value.filter { it.isDigit() }
                if (!isExpanded) isExpanded = true
            },
            label = { Text(stringResource(Res.string.editor_field_year_placeholder)) },
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
                filterText = selectedYear
            },
        ) {
            filteredYears.forEach { year ->
                DropdownMenuItem(
                    text = { Text(year) },
                    onClick = {
                        onYearSelected(year)
                        filterText = year
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipInputField(
    label: String,
    placeholder: String,
    chips: List<String>,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit,
) {
    var inputText by remember { mutableStateOf("") }
    val commitChip = {
        val trimmed = inputText.trim()
        if (trimmed.isNotEmpty() && trimmed !in chips) {
            onAdd(trimmed)
            inputText = ""
        }
    }
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (chips.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                chips.forEach { chip ->
                    InputChip(
                        selected = false,
                        onClick = { onRemove(chip) },
                        label = { Text(chip) },
                        trailingIcon = {
                            Icon(
                                imageVector = CloseIcon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text(placeholder) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { commitChip() }),
                modifier = Modifier
                    .weight(1f)
                    .onKeyEvent { event ->
                        if (event.key == Key.Enter) {
                            commitChip()
                            true
                        } else {
                            false
                        }
                    },
            )
            OutlinedButton(
                onClick = { commitChip() },
                enabled = inputText.isNotBlank(),
            ) {
                Text(stringResource(Res.string.editor_chip_action_add))
            }
        }
    }
}

@Composable
private fun MonumentsSection(
    items: List<UiMonument>,
    onItemUpdate: (Int, UiMonument) -> Unit,
    onItemRemove: (Int) -> Unit,
    onAddMonument: () -> Unit
) {
    Text(
        text = stringResource(Res.string.editor_section_monument_title),
        style = MaterialTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(8.dp))
    items.forEachIndexed { index, monument ->
        MonumentRow(
            monument = monument,
            onUpdate = { item -> onItemUpdate(index, item) },
            onRemove = { onItemRemove(index) },
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
    OutlinedButton(onClick = onAddMonument) {
        Text(stringResource(Res.string.editor_monument_action_add))
    }
}

@Composable
private fun MonumentRow(
    monument: UiMonument,
    onUpdate: (UiMonument) -> Unit,
    onRemove: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = monument.name.value,
                onValueChange = { onUpdate(monument.copy(name = MonumentName(it))) },
                label = { Text(stringResource(Res.string.editor_monument_field_name)) },
                singleLine = true,
                modifier = Modifier.weight(2f),
            )
            OutlinedTextField(
                value = monument.type.value,
                onValueChange = { onUpdate(monument.copy(type = MonumentType(it))) },
                label = { Text(stringResource(Res.string.editor_monument_field_type)) },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = monument.culture.value,
                onValueChange = { onUpdate(monument.copy(culture = MonumentCulture(it))) },
                label = { Text(stringResource(Res.string.editor_monument_field_culture)) },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = monument.period.value,
                onValueChange = { onUpdate(monument.copy(period = MonumentPeriod(it))) },
                label = { Text(stringResource(Res.string.editor_monument_field_period)) },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = monument.geographicLocation.value,
                onValueChange = { onUpdate(monument.copy(geographicLocation = MonumentLocation(it))) },
                label = { Text(stringResource(Res.string.editor_monument_field_location)) },
                singleLine = true,
                modifier = Modifier.weight(1.5f),
            )
            OutlinedTextField(
                value = monument.number.value,
                onValueChange = { onUpdate(monument.copy(number = MonumentNumber(it))) },
                label = { Text(stringResource(Res.string.editor_monument_field_number)) },
                singleLine = true,
                modifier = Modifier.weight(0.5f),
            )
            TextButton(onClick = onRemove) {
                Text(
                    text = stringResource(Res.string.editor_monument_action_remove),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun ActionButtons(
    isSaving: Boolean,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = onSave,
            enabled = !isSaving,
        ) {
            Text(
                if (isSaving) stringResource(Res.string.editor_action_saving)
                else stringResource(Res.string.editor_action_save)
            )
        }
        OutlinedButton(onClick = onCancel) {
            Text(stringResource(Res.string.editor_action_cancel))
        }
    }
}
