package ru.arheo.feature.report_editor.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.arheo.core.domain.model.Monument
import ru.arheo.core.util.getStore

interface ReportEditorComponent {

    val state: StateFlow<ReportEditorStore.State>

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
}

class DefaultReportEditorComponent(
    componentContext: ComponentContext,
    private val reportEditorStoreFactory: ReportEditorStoreFactory,
    reportId: Long?,
    private val output: (ReportEditorComponent.Output) -> Unit,
) : ReportEditorComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val store: ReportEditorStore =
        instanceKeeper.getStore("ReportEditorStore") {
            reportEditorStoreFactory.create(reportId)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ReportEditorStore.State> = store.stateFlow

    init {
        lifecycle.subscribe(onDestroy = { scope.cancel() })
        scope.launch {
            @OptIn(ExperimentalCoroutinesApi::class)
            store.labels.collect { label ->
                when (label) {
                    is ReportEditorStore.Label.Saved -> output(ReportEditorComponent.Output.Saved)
                }
            }
        }
    }

    override fun onTitleChanged(title: String) {
        store.accept(ReportEditorStore.Intent.UpdateTitle(title))
    }

    override fun onYearChanged(year: String) {
        store.accept(ReportEditorStore.Intent.UpdateYear(year))
    }

    override fun onAuthorsChanged(authors: String) {
        store.accept(ReportEditorStore.Intent.UpdateAuthors(authors))
    }

    override fun onWorkTypeChanged(workType: String) {
        store.accept(ReportEditorStore.Intent.UpdateWorkType(workType))
    }

    override fun onDistrictsChanged(districts: String) {
        store.accept(ReportEditorStore.Intent.UpdateDistricts(districts))
    }

    override fun onKeywordsChanged(keywords: String) {
        store.accept(ReportEditorStore.Intent.UpdateKeywords(keywords))
    }

    override fun onUpdateMonument(index: Int, monument: Monument) {
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

    override fun onCancel() {
        output(ReportEditorComponent.Output.Cancelled)
    }
}
