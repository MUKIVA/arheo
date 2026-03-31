package ru.arheo.feature.report.editor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arheo.feature.report_editor.impl.generated.resources.Res
import arheo.feature.report_editor.impl.generated.resources.editor_field_authors_placeholder
import arheo.feature.report_editor.impl.generated.resources.editor_field_districts_placeholder
import arheo.feature.report_editor.impl.generated.resources.editor_field_keywords_placeholder
import arheo.feature.report_editor.impl.generated.resources.editor_field_name_placeholder
import arheo.feature.report_editor.impl.generated.resources.editor_field_work_type_placeholder
import arheo.feature.report_editor.impl.generated.resources.editor_field_year_placeholder
import arheo.feature.report_editor.impl.generated.resources.editor_title_create
import arheo.feature.report_editor.impl.generated.resources.editor_title_edit
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import ru.arheo.feature.report.editor.domian.models.monument.MonumentCulture
import ru.arheo.feature.report.editor.domian.models.monument.MonumentLocation
import ru.arheo.feature.report.editor.domian.models.monument.MonumentName
import ru.arheo.feature.report.editor.domian.models.monument.MonumentNumber
import ru.arheo.feature.report.editor.domian.models.monument.MonumentPeriod
import ru.arheo.feature.report.editor.domian.models.monument.MonumentType
import ru.arheo.feature.report.editor.presentation.ReportEditorStore
import ru.arheo.feature.report.editor.presentation.models.UiMonument

@Composable
internal fun ReportEditorContent(
    state: ReportEditorStore.State.Content,
    reportSelector: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onReportNameChanged: (String) -> Unit = {},
    onReportYearChanged: (String) -> Unit = {},
    onReportAuthorsChanged: (String) -> Unit = {},
    onReportWorkTypeChanged: (String) -> Unit = {},
    onReportDistrictsChanged: (String) -> Unit = {},
    onReportKeywordsChanged: (String) -> Unit = {},
    onMonumentItemUpdate: (Int, UiMonument) -> Unit = { index, item -> },
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
            text = state.error.orEmpty(),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
    MetadataSection(
        state = state,
        onReportNameChanged = onReportNameChanged,
        onReportYearChanged = onReportYearChanged,
        onReportAuthorsChanged = onReportAuthorsChanged,
        onReportWorkTypeChanged = onReportWorkTypeChanged,
        onReportDistrictsChanged = onReportDistrictsChanged,
        onReportKeywordsChanged = onReportKeywordsChanged
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

@Composable
private fun MetadataSection(
    state: ReportEditorStore.State.Content,
    onReportNameChanged: (String) -> Unit = {},
    onReportYearChanged: (String) -> Unit = {},
    onReportAuthorsChanged: (String) -> Unit = {},
    onReportWorkTypeChanged: (String) -> Unit = {},
    onReportDistrictsChanged: (String) -> Unit = {},
    onReportKeywordsChanged: (String) -> Unit = {}
){
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
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = state.year,
            onValueChange = onReportYearChanged,
            label = { Text(stringResource(Res.string.editor_field_year_placeholder)) },
            singleLine = true,
            modifier = Modifier.width(120.dp),
        )
        OutlinedTextField(
            value = state.authors,
            onValueChange = onReportAuthorsChanged,
            label = { Text(stringResource(Res.string.editor_field_authors_placeholder)) },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = state.workType,
            onValueChange = onReportWorkTypeChanged,
            label = { Text(stringResource(Res.string.editor_field_work_type_placeholder)) },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
        OutlinedTextField(
            value = state.districts,
            onValueChange = onReportDistrictsChanged,
            label = { Text(stringResource(Res.string.editor_field_districts_placeholder)) },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = state.keywords,
        onValueChange = onReportKeywordsChanged,
        label = { Text(stringResource(Res.string.editor_field_keywords_placeholder)) },
        minLines = 2,
        maxLines = 4,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun MonumentsSection(
    items: List<UiMonument>,
    onItemUpdate: (Int, UiMonument) -> Unit,
    onItemRemove: (Int) -> Unit,
    onAddMonument: () -> Unit
) {
    Text(text = "Памятники", style = MaterialTheme.typography.titleMedium)
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
        Text("+ Добавить памятник")
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
                label = { Text("Название *") },
                singleLine = true,
                modifier = Modifier.weight(2f),
            )
            OutlinedTextField(
                value = monument.type.value,
                onValueChange = { onUpdate(monument.copy(type = MonumentType(it))) },
                label = { Text("Тип") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = monument.culture.value,
                onValueChange = { onUpdate(monument.copy(culture = MonumentCulture(it))) },
                label = { Text("Культура") },
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
                label = { Text("Период") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = monument.geographicLocation.value,
                onValueChange = { onUpdate(monument.copy(geographicLocation = MonumentLocation(it))) },
                label = { Text("Расположение") },
                singleLine = true,
                modifier = Modifier.weight(1.5f),
            )
            OutlinedTextField(
                value = monument.number.value,
                onValueChange = { onUpdate(monument.copy(number = MonumentNumber(it))) },
                label = { Text("№") },
                singleLine = true,
                modifier = Modifier.weight(0.5f),
            )
            TextButton(onClick = onRemove) {
                Text("Удалить", color = MaterialTheme.colorScheme.error)
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
            Text(if (isSaving) "Сохранение..." else "Сохранить")
        }
        OutlinedButton(onClick = onCancel) {
            Text("Отмена")
        }
    }
}
