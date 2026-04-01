package ru.arheo.feature.report.editor.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var selectorWorkingDir by remember { mutableStateOf("") }
    var selectorHasFiles by remember { mutableStateOf(false) }

    when (val instance = state) {
        is ReportEditorStore.State.Content ->
            ReportEditorContent(
                state = instance,
                reportSelector = {
                    reportSelector.launch(
                        componentContext = component,
                        modifier = Modifier.fillMaxWidth(),
                        archiveFilePath = instance.archiveFilePath,
                        onFileStateChanged = { workingDir, hasFiles ->
                            selectorWorkingDir = workingDir
                            selectorHasFiles = hasFiles
                        },
                    )
                },
                onReportNameChanged      = component::onNameChanged,
                onReportYearChanged      = component::onYearChanged,
                onReportWorkTypeChanged  = component::onWorkTypeChanged,
                onAddAuthor              = component::onAddAuthor,
                onRemoveAuthor           = component::onRemoveAuthor,
                onAddDistrict            = component::onAddDistrict,
                onRemoveDistrict         = component::onRemoveDistrict,
                onAddKeyword             = component::onAddKeyword,
                onRemoveKeyword          = component::onRemoveKeyword,
                onMonumentItemUpdate     = component::onUpdateMonument,
                onMonumentItemRemove     = component::onRemoveMonument,
                onMonumentItemAdd        = component::onAddMonument,
                onSaveReport             = {
                    component.onSave(selectorWorkingDir, selectorHasFiles)
                },
                onCancel                 = component::onCancel,
                modifier = modifier
            )
        is ReportEditorStore.State.Loading ->
            ReportEditorLoading(modifier)
        is ReportEditorStore.State.Error ->
            ReportEditorError(modifier)
    }
}
