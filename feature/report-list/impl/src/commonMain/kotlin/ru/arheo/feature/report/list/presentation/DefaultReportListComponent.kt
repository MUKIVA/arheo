package ru.arheo.feature.report.list.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import ru.arheo.core.util.getStore

internal class DefaultReportListComponent(
    componentContext: ComponentContext,
    private val reportListStoreFactory: ReportListStoreFactory,
    private val navigateCreateReport: () -> Unit,
    private val navigateEditReport: (Long) -> Unit,
    private val navigateViewReport: (Long) -> Unit
) : ReportListComponent, ComponentContext by componentContext {

    private val store: ReportListStore by lazy {
        instanceKeeper.getStore(ReportListStoreFactory.STORE_NAME) {
            reportListStoreFactory.create()
        }
    }

    @ExperimentalCoroutinesApi
    override val state: StateFlow<ReportListStore.State> = store.stateFlow

    init {
        lifecycle.subscribe(
            onResume = { store.accept(ReportListStore.Intent.Refresh) },
        )
    }

    override fun onSearchQueryChanged(query: String) {
        store.accept(ReportListStore.Intent.UpdateSearchQuery(query))
    }

    override fun onEditReport(reportId: Long) {
        navigateEditReport(reportId)
    }

    override fun onCreateReport() {
        navigateCreateReport()
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

    override fun onOpenReport(reportId: Long) {
        navigateViewReport(reportId)
    }
}
