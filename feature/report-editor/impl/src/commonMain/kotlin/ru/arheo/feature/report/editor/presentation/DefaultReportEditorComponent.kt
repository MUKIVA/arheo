package ru.arheo.feature.report.editor.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.arheo.core.util.getStore
import ru.arheo.feature.report.editor.presentation.models.UiMonument

internal class DefaultReportEditorComponent(
    componentContext: ComponentContext,
    reportId: Long?,
    private val reportEditorStoreFactory: ReportEditorStoreFactory,
    private val navigateBack: () -> Unit
) : ReportEditorComponent, ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val store: ReportEditorStore =
        instanceKeeper.getStore("ReportEditorStore") {
            reportEditorStoreFactory.create(reportId)
        }

    private val mutableEvents = MutableSharedFlow<ReportEditorComponent.Event>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ReportEditorStore.State> = store.stateFlow

    override val events: Flow<ReportEditorComponent.Event> = mutableEvents.asSharedFlow()

    init {
        lifecycle.subscribe(onDestroy = { coroutineScope.cancel() })
        coroutineScope.launch {
            @OptIn(ExperimentalCoroutinesApi::class)
            store.labels.collect { label ->
                when (label) {
                    is ReportEditorStore.Label.Saved ->
                        navigateBack()
                    is ReportEditorStore.Label.SaveError ->
                        mutableEvents.emit(ReportEditorComponent.Event.ShowValidationError(label.error))
                }
            }
        }
    }

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

    override fun onSave(workingDirectory: String, hasFiles: Boolean) {
        store.accept(ReportEditorStore.Intent.Save(workingDirectory, hasFiles))
    }

    override fun onCancel() {
        navigateBack()
    }
}
