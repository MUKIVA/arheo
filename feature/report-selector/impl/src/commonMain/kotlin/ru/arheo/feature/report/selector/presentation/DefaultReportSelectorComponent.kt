package ru.arheo.feature.report.selector.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import ru.arheo.core.util.getStore

internal class DefaultReportSelectorComponent(
    componentContext: ComponentContext,
    private val reportSelectorStoreFactory: ReportSelectorStoreFactory,
) : ReportSelectorComponent, ComponentContext by componentContext {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val store: ReportSelectorStore =
        instanceKeeper.getStore("ReportSelectorStore") {
            reportSelectorStoreFactory.create()
        }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    override val state: StateFlow<ReportSelectorComponent.State> =
//        store.stateFlow
//            .map { storeState -> storeState.toComponentState() }
//            .stateIn(coroutineScope, SharingStarted.Eagerly, ReportSelectorComponent.State())

    init {
        lifecycle.subscribe(onDestroy = {
            coroutineScope.cancel()
        })
    }

    override fun onAttachFiles(paths: List<String>) {
        store.accept(ReportSelectorStore.Intent.AttachFiles(paths))
    }

    override fun onRemoveFile(fileName: String) {
        store.accept(ReportSelectorStore.Intent.RemoveFile(fileName))
    }

    override fun onDragOver(isDragging: Boolean) {
        store.accept(ReportSelectorStore.Intent.UpdateDragOver(isDragging))
    }

    override fun loadArchive(archivePath: String) {
        store.accept(ReportSelectorStore.Intent.LoadArchive(archivePath))
    }

//    private fun ReportSelectorStore.State.toComponentState(): ReportSelectorComponent.State =
//        ReportSelectorComponent.State(
//            attachedFiles = attachedFiles,
//            isDraggingOver = isDraggingOver,
//            workingDirectory = workingDirectory,
//            isLoading = isLoading,
//        )
}
