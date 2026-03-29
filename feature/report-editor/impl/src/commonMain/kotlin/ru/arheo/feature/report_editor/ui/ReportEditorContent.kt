package ru.arheo.feature.report_editor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.arheo.core.domain.model.Monument
import ru.arheo.feature.report_editor.presentation.ReportEditorComponent
import ru.arheo.feature.report_editor.presentation.ReportEditorStore
import ru.arheo.feature.report_selector.ui.ReportSelectorContent

@Composable
fun ReportEditorContent(
    component: ReportEditorComponent,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text("ReportEditorContent")
    }
//    val state by component.state.collectAsState()
//    if (state.isLoading) {
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            CircularProgressIndicator()
//        }
//        return
//    }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp)
//            .verticalScroll(rememberScrollState()),
//    ) {
//        Text(
//            text = if (state.isEditing) "Редактирование отчёта" else "Добавление отчёта",
//            style = MaterialTheme.typography.headlineMedium,
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        if (state.error != null) {
//            Text(
//                text = state.error.orEmpty(),
//                color = MaterialTheme.colorScheme.error,
//                style = MaterialTheme.typography.bodyMedium,
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//        }
//        MetadataSection(state, component)
//        Spacer(modifier = Modifier.height(16.dp))
//        HorizontalDivider()
//        Spacer(modifier = Modifier.height(8.dp))
//        MonumentsSection(state.monuments, component)
//        Spacer(modifier = Modifier.height(16.dp))
//        HorizontalDivider()
//        Spacer(modifier = Modifier.height(8.dp))
//        ReportSelectorContent(component.selectorComponent)
//        Spacer(modifier = Modifier.height(24.dp))
//        HorizontalDivider()
//        Spacer(modifier = Modifier.height(16.dp))
//        ActionButtons(state.isSaving, component)
//    }
}

@Composable
private fun MetadataSection(
    state: ReportEditorStore.State,
    component: ReportEditorComponent,
) {
    OutlinedTextField(
        value = state.title,
        onValueChange = component::onTitleChanged,
        label = { Text("Название отчёта *") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = state.year,
            onValueChange = component::onYearChanged,
            label = { Text("Год *") },
            singleLine = true,
            modifier = Modifier.width(120.dp),
        )
        OutlinedTextField(
            value = state.authors,
            onValueChange = component::onAuthorsChanged,
            label = { Text("Авторы * (через запятую)") },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = state.workType,
            onValueChange = component::onWorkTypeChanged,
            label = { Text("Тип работ") },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
        OutlinedTextField(
            value = state.districts,
            onValueChange = component::onDistrictsChanged,
            label = { Text("Районы (через запятую)") },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = state.keywords,
        onValueChange = component::onKeywordsChanged,
        label = { Text("Ключевые слова (через запятую)") },
        minLines = 2,
        maxLines = 4,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun MonumentsSection(
    monuments: List<Monument>,
    component: ReportEditorComponent,
) {
    Text(text = "Памятники", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    monuments.forEachIndexed { index, monument ->
        MonumentRow(
            monument = monument,
            onUpdate = { component.onUpdateMonument(index, it) },
            onRemove = { component.onRemoveMonument(index) },
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
    OutlinedButton(onClick = component::onAddMonument) {
        Text("+ Добавить памятник")
    }
}

@Composable
private fun ActionButtons(
    isSaving: Boolean,
    component: ReportEditorComponent,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = component::onSave,
            enabled = !isSaving,
        ) {
            Text(if (isSaving) "Сохранение..." else "Сохранить")
        }
        OutlinedButton(onClick = component::onCancel) {
            Text("Отмена")
        }
    }
}

@Composable
private fun MonumentRow(
    monument: Monument,
    onUpdate: (Monument) -> Unit,
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
                value = monument.name,
                onValueChange = { onUpdate(monument.copy(name = it)) },
                label = { Text("Название *") },
                singleLine = true,
                modifier = Modifier.weight(2f),
            )
            OutlinedTextField(
                value = monument.type,
                onValueChange = { onUpdate(monument.copy(type = it)) },
                label = { Text("Тип") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = monument.culture,
                onValueChange = { onUpdate(monument.copy(culture = it)) },
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
                value = monument.period,
                onValueChange = { onUpdate(monument.copy(period = it)) },
                label = { Text("Период") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            OutlinedTextField(
                value = monument.geographicLocation,
                onValueChange = { onUpdate(monument.copy(geographicLocation = it)) },
                label = { Text("Расположение") },
                singleLine = true,
                modifier = Modifier.weight(1.5f),
            )
            OutlinedTextField(
                value = monument.number,
                onValueChange = { onUpdate(monument.copy(number = it)) },
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
