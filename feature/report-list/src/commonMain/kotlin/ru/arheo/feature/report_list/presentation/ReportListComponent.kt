package ru.arheo.feature.report_list.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import ru.arheo.core.util.getStore

interface ReportListComponent {

    val state: StateFlow<ReportListStore.State>

    fun onSearchQueryChanged(query: String)
    fun onEditReport(reportId: Long)
    fun onCreateReport()
    fun onRequestDeleteReport(reportId: Long)
    fun onConfirmDeleteReport()
    fun onDismissDeleteReport()

    sealed interface Output {
        data class EditReport(val reportId: Long) : Output
        data object CreateReport : Output
    }
}

class DefaultReportListComponent(
    componentContext: ComponentContext,
    private val reportListStoreFactory: ReportListStoreFactory,
    private val output: (ReportListComponent.Output) -> Unit,
) : ReportListComponent, ComponentContext by componentContext {

    private val store: ReportListStore =
        instanceKeeper.getStore("ReportListStore") {
            reportListStoreFactory.create()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ReportListStore.State> = store.stateFlow

    override fun onSearchQueryChanged(query: String) {
        store.accept(ReportListStore.Intent.UpdateSearchQuery(query))
    }

    override fun onEditReport(reportId: Long) {
        output(ReportListComponent.Output.EditReport(reportId))
    }

    override fun onCreateReport() {
        output(ReportListComponent.Output.CreateReport)
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
