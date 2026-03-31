package ru.arheo.feature.report.editor.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
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

//    override val selectorComponent: ReportSelectorComponent =
//        selectorFactory.create(componentContext)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ReportEditorStore.State> = store.stateFlow

    init {
//        lifecycle.subscribe(onDestroy = {
//            coroutineScope.cancel()
//            koinScope.close()
//        })
//        coroutineScope.launch {
//            @OptIn(ExperimentalCoroutinesApi::class)
//            store.labels.collect { label ->
//                when (label) {
//                    is ReportEditorStore.Label.Saved -> output(ReportEditorComponent.Output.Saved)
//                    is ReportEditorStore.Label.ArchivePathLoaded -> {
//                        label.archivePath?.let { selectorComponent.loadArchive(it) }
//                    }
//                }
//            }
//        }
    }

    override fun onNameChanged(name: String) {
        store.accept(ReportEditorStore.Intent.UpdateName(name))
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
//        val selectorState = selectorComponent.state.value
//        store.accept(
//            ReportEditorStore.Intent.Save(
//                workingDirectory = selectorState.workingDirectory,
//                hasFiles = selectorState.hasFiles,
//            )
//        )
        navigateBack()
    }

    override fun onCancel() {
        navigateBack()
    }
}
