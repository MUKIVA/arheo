package ru.arheo.feature.report.editor.ui.static

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import arheo.feature.report_editor.impl.generated.resources.*
import ru.arheo.feature.report.editor.presentation.ReportEditorComponent
import ru.arheo.feature.report.editor.presentation.ReportEditorStore
import ru.arheo.feature.report.editor.ui.models.DynamicFieldActions
import ru.arheo.feature.report.editor.ui.models.DynamicFieldState

internal object DynamicFieldDefaults {

    @Composable
    fun rememberDynamicFields(
        component: ReportEditorComponent,
        state: ReportEditorStore.State.Content
    ) = remember(component, state) {
        listOf(
            DynamicFieldState(
                labelRes = Res.string.editor_field_authors_label,
                placeholderRes = Res.string.editor_field_authors_input_placeholder,
                emptyMessageRes = Res.string.editor_field_authors_empty,
                data = derivedStateOf { state.authors },
                actions = object : DynamicFieldActions {
                    override fun onAdd(value: String) = component.onAddAuthor(value)
                    override fun onRemove(value: String) = component.onRemoveAuthor(value)
                }
            ),
            DynamicFieldState(
                labelRes = Res.string.editor_field_districts_label,
                placeholderRes = Res.string.editor_field_districts_input_placeholder,
                emptyMessageRes = Res.string.editor_field_districts_empty,
                data = derivedStateOf { state.districts },
                actions = object : DynamicFieldActions {
                    override fun onAdd(value: String) = component.onAddDistrict(value)
                    override fun onRemove(value: String) = component.onRemoveDistrict(value)
                }
            ),
            DynamicFieldState(
                labelRes = Res.string.editor_field_keywords_label,
                placeholderRes = Res.string.editor_field_keywords_input_placeholder,
                emptyMessageRes = Res.string.editor_field_keywords_empty,
                data = derivedStateOf { state.keywords },
                actions = object : DynamicFieldActions {
                    override fun onAdd(value: String) = component.onAddKeyword(value)
                    override fun onRemove(value: String) = component.onRemoveKeyword(value)
                }
            )
        )
    }

}