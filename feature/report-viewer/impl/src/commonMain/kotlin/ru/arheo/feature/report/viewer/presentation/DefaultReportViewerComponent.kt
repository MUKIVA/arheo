package ru.arheo.feature.report.viewer.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

internal class DefaultReportViewerComponent(
    componentContext: ComponentContext,
    reportViewerStoreFactory: ReportViewerStoreFactory,
    private val navigateBack: () -> Unit
) : ReportViewerComponent, ComponentContext by componentContext {

    private val store: ReportViewerStore by lazy {
        reportViewerStoreFactory.create()
    }

    init {
        lifecycle.subscribe(
            onDestroy = { store.dispose() }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ReportViewerStore.State>
        get() = store.stateFlow

    override fun refresh() {
        store.accept(ReportViewerStore.Intent.Loading)
        store.accept(ReportViewerStore.Intent.Refresh)
    }

    override fun openMaterials() {
        store.accept(ReportViewerStore.Intent.OpenMaterials)
    }

    override fun back() = navigateBack()

}