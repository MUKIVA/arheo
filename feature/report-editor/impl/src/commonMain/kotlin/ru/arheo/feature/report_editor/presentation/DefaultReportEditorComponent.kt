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
import org.koin.core.scope.Scope
import ru.arheo.core.domain.model.Monument
import ru.arheo.core.util.getStore
import ru.arheo.feature.report_selector.presentation.ReportSelectorComponent

internal class DefaultReportEditorComponent(
    componentContext: ComponentContext,
    private val reportEditorStoreFactory: ReportEditorStoreFactory,
    selectorFactory: ReportSelectorComponent.Factory,
    reportId: Long?,
    private val output: (ReportEditorComponent.Output) -> Unit,
    private val koinScope: Scope,
) : ReportEditorComponent, ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val store: ReportEditorStore =
        instanceKeeper.getStore("ReportEditorStore") {
            reportEditorStoreFactory.create(reportId)
        }

    override val selectorComponent: ReportSelectorComponent =
        selectorFactory.create(componentContext)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ReportEditorStore.State> = store.stateFlow

    init {
        lifecycle.subscribe(onDestroy = {
            coroutineScope.cancel()
            koinScope.close()
        })
        coroutineScope.launch {
            @OptIn(ExperimentalCoroutinesApi::class)
            store.labels.collect { label ->
                when (label) {
                    is ReportEditorStore.Label.Saved -> output(ReportEditorComponent.Output.Saved)
                    is ReportEditorStore.Label.ArchivePathLoaded -> {
                        label.archivePath?.let { selectorComponent.loadArchive(it) }
                    }
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
        val selectorState = selectorComponent.state.value
        store.accept(
            ReportEditorStore.Intent.Save(
                workingDirectory = selectorState.workingDirectory,
                hasFiles = selectorState.hasFiles,
            )
        )
    }

    override fun onCancel() {
        output(ReportEditorComponent.Output.Cancelled)
    }
}
