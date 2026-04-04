package ru.arheo.feature.report.editor.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.arheo.feature.report.editor.presentation.models.SaveValidationError
import ru.arheo.feature.report.editor.presentation.models.UiMonument

internal interface ReportEditorComponent : ComponentContext {
    val state: StateFlow<ReportEditorStore.State>
    val events: Flow<Event>

    sealed interface Event {
        data class ShowValidationError(val error: SaveValidationError) : Event
    }

    fun onNameChanged(name: String)
    fun onYearChanged(year: String)
    fun onWorkTypeChanged(workType: String)
    fun onAddAuthor(author: String)
    fun onRemoveAuthor(author: String)
    fun onAddDistrict(district: String)
    fun onRemoveDistrict(district: String)
    fun onAddKeyword(keyword: String)
    fun onRemoveKeyword(keyword: String)
    fun onUpdateMonument(index: Int, monument: UiMonument)
    fun onAddMonument()
    fun onRemoveMonument(index: Int)
    fun onSave(workingDirectory: String, hasFiles: Boolean)
    fun onCancel()
}
