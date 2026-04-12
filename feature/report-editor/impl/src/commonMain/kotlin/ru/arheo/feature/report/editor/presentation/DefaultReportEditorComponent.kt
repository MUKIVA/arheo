package ru.arheo.feature.report.editor.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.arheo.feature.report.editor.presentation.models.UiMonument

internal class DefaultReportEditorComponent(
    componentContext: ComponentContext,
    reportId: Long?,
    private val reportEditorStoreFactory: ReportEditorStoreFactory,
    private val navigateBack: () -> Unit
) : ReportEditorComponent, ComponentContext by componentContext {

    private val store: ReportEditorStore by lazy {
        reportEditorStoreFactory.create(reportId)
    }

    init {
        lifecycle.subscribe(
            onDestroy = { store.dispose() }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ReportEditorStore.State> = store.stateFlow

    override val events: Flow<ReportEditorStore.Label> = store.labels

    override fun onNameChanged(name: String) {
        store.accept(ReportEditorStore.Intent.UpdateName(name))
    }

    override fun onYearChanged(year: String) {
        store.accept(ReportEditorStore.Intent.UpdateYear(year))
    }

    override fun onWorkTypeChanged(workType: String) {
        store.accept(ReportEditorStore.Intent.UpdateWorkType(workType))
    }

    override fun onAddAuthor(author: String) {
        store.accept(ReportEditorStore.Intent.AddAuthor(author))
    }

    override fun onRemoveAuthor(author: String) {
        store.accept(ReportEditorStore.Intent.RemoveAuthor(author))
    }

    override fun onAddDistrict(district: String) {
        store.accept(ReportEditorStore.Intent.AddDistrict(district))
    }

    override fun onRemoveDistrict(district: String) {
        store.accept(ReportEditorStore.Intent.RemoveDistrict(district))
    }

    override fun onAddKeyword(keyword: String) {
        store.accept(ReportEditorStore.Intent.AddKeyword(keyword))
    }

    override fun onRemoveKeyword(keyword: String) {
        store.accept(ReportEditorStore.Intent.RemoveKeyword(keyword))
    }

    override fun onUpdateMonument(index: Int, monument: UiMonument) {
        store.accept(ReportEditorStore.Intent.UpdateMonument(index, monument))
    }

    override fun onAddMonument() {
        store.accept(ReportEditorStore.Intent.AddMonument)
    }

    override fun onRemoveMonument(index: Int) {
        store.accept(ReportEditorStore.Intent.RemoveMonument(index))
    }

    override fun onSave() {
        store.accept(ReportEditorStore.Intent.Save)
    }

    override fun onBack() {
        navigateBack()
    }
}
