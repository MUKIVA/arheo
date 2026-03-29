package ru.arheo.feature.report.list.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import ru.arheo.core.util.getStore

internal class DefaultReportListComponent(
    componentContext: ComponentContext,
    private val reportListStoreFactory: ReportListStoreFactory,
) : ReportListComponent, ComponentContext by componentContext {

    init {
//        lifecycle.subscribe(
//            onResume = { store.accept(ReportListStore.Intent.Refresh) },
//            onDestroy = { koinScope.close() },
//        )
    }

    private val store: ReportListStore =
        instanceKeeper.getStore("ReportListStore") {
            reportListStoreFactory.create()
        }

    @ExperimentalCoroutinesApi
    override val state: StateFlow<ReportListStore.State> = store.stateFlow

    override fun onSearchQueryChanged(query: String) {
        store.accept(ReportListStore.Intent.UpdateSearchQuery(query))
    }

    override fun onEditReport(reportId: Long) {
//        output(ReportListComponent.Output.EditReport(reportId))
    }

    override fun onCreateReport() {
//        output(ReportListComponent.Output.CreateReport)
    }

    override fun onRequestDeleteReport(reportId: Long) {
        store.accept(ReportListStore.Intent.RequestDeleteReport(reportId))
    }

    override fun onConfirmDeleteReport() {
        store.accept(ReportListStore.Intent.ConfirmDeleteReport)
    }

    override fun onDismissDeleteReport() {
        store.accept(ReportListStore.Intent.DismissDeleteReport)
    }
}
