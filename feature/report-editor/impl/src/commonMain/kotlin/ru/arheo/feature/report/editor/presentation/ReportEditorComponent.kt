package ru.arheo.feature.report.editor.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.arheo.feature.report.editor.presentation.models.UiMonument

internal interface ReportEditorComponent : ComponentContext {
    val state: StateFlow<ReportEditorStore.State>

    fun onNameChanged(name: String)
    fun onYearChanged(year: String)
    fun onAuthorsChanged(authors: String)
    fun onWorkTypeChanged(workType: String)
    fun onDistrictsChanged(districts: String)
    fun onKeywordsChanged(keywords: String)
    fun onUpdateMonument(index: Int, monument: UiMonument)
    fun onAddMonument()
    fun onRemoveMonument(index: Int)
    fun onSave()
    fun onCancel()
}
