package ru.arheo.feature.report.editor.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import arheo.feature.report_editor.impl.generated.resources.Res
import arheo.feature.report_editor.impl.generated.resources.editor_error_empty_title
import arheo.feature.report_editor.impl.generated.resources.editor_error_invalid_year
import arheo.feature.report_editor.impl.generated.resources.editor_error_no_authors
import arheo.feature.report_editor.impl.generated.resources.editor_error_save_failed
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import ru.arheo.feature.report.editor.presentation.ReportEditorComponent
import ru.arheo.feature.report.editor.presentation.ReportEditorStore
import ru.arheo.feature.report.editor.presentation.models.SaveValidationError
import ru.arheo.feature.report.selector.ReportSelectorFeatureLauncher

@Composable
internal fun ReportEditorRoot(
    modifier: Modifier = Modifier,
    reportSelector: ReportSelectorFeatureLauncher = koinInject(),
    component: ReportEditorComponent = koinInject(),
) {
    val state by component.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessages = rememberErrorMessages()

    LaunchedEffect(component) {
        component.events.collect { event ->
            when (event) {
                is ReportEditorStore.Label.SaveError -> snackbarHostState
                    .showSnackbar(errorMessages.getValue(event.error))
                is ReportEditorStore.Label.Saved -> component.onBack()
            }
        }
    }

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

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun rememberErrorMessages(): Map<SaveValidationError, String> {
    val emptyTitle = stringResource(Res.string.editor_error_empty_title)
    val invalidYear = stringResource(Res.string.editor_error_invalid_year)
    val noAuthors = stringResource(Res.string.editor_error_no_authors)
    val saveFailed = stringResource(Res.string.editor_error_save_failed)

    return remember {
        mapOf(
            SaveValidationError.EMPTY_TITLE to emptyTitle,
            SaveValidationError.INVALID_YEAR to invalidYear,
            SaveValidationError.NO_AUTHORS to noAuthors,
            SaveValidationError.SAVE_FAILED to saveFailed,
        )
    }
}
