package ru.arheo.feature.report.editor.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.arheo.feature.report.editor.presentation.models.UiMonument

internal interface ReportEditorComponent : ComponentContext {
    val state: StateFlow<ReportEditorStore.State>

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
