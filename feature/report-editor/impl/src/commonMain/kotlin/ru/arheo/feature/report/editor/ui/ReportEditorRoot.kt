package ru.arheo.feature.report.editor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import org.koin.compose.koinInject
import ru.arheo.core.domain.model.Monument
import ru.arheo.feature.report.editor.presentation.ReportEditorComponent
import ru.arheo.feature.report.editor.presentation.ReportEditorStore

@Composable
internal fun ReportEditorRoot(
    modifier: Modifier = Modifier,
    component: ReportEditorComponent = koinInject(),
) {
    val state by component.state.collectAsState()
    val scrollState = rememberScrollState()

    when (val instance = state) {
        is ReportEditorStore.State.Content ->
            ReportEditorContent(
                state = instance,
                onReportNameChanged      = component::onNameChanged,
                onReportYearChanged      = component::onYearChanged,
                onReportAuthorsChanged   = component::onAuthorsChanged,
                onReportWorkTypeChanged  = component::onWorkTypeChanged,
                onReportDistrictsChanged = component::onDistrictsChanged,
                onReportKeywordsChanged  = component::onKeywordsChanged,
                onMonumentItemUpdate     = component::onUpdateMonument,
                onMonumentItemRemove     = component::onRemoveMonument,
                onMonumentItemAdd        = component::onAddMonument,
                onSaveReport             = component::onSave,
                onCancel                 = component::onCancel,
                modifier = modifier.verticalScroll(scrollState)
            )
        is ReportEditorStore.State.Loading ->
            ReportEditorLoading(modifier)
        is ReportEditorStore.State.Error ->
            ReportEditorError(modifier)

    }
}
