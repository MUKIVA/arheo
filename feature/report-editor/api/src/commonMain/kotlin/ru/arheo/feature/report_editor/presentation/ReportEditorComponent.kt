package ru.arheo.feature.report_editor.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import ru.arheo.core.domain.model.Monument
import ru.arheo.feature.report_selector.presentation.ReportSelectorComponent

interface ReportEditorComponent {

    val state: StateFlow<ReportEditorStore.State>
    val selectorComponent: ReportSelectorComponent

    fun onTitleChanged(title: String)
    fun onYearChanged(year: String)
    fun onAuthorsChanged(authors: String)
    fun onWorkTypeChanged(workType: String)
    fun onDistrictsChanged(districts: String)
    fun onKeywordsChanged(keywords: String)
    fun onUpdateMonument(index: Int, monument: Monument)
    fun onAddMonument()
    fun onRemoveMonument(index: Int)
    fun onSave()
    fun onCancel()

    sealed interface Output {
        data object Saved : Output
        data object Cancelled : Output
    }

    fun interface Factory {
        fun create(
            componentContext: ComponentContext,
            reportId: Long?,
            output: (Output) -> Unit,
        ): ReportEditorComponent
    }
}
