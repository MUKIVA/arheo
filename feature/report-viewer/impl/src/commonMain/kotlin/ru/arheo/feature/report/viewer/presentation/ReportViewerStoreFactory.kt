package ru.arheo.feature.report.viewer.presentation

import com.arkivanov.mvikotlin.core.store.StoreFactory

internal class ReportViewerStoreFactory(
    private val storeFactory: StoreFactory,
    private val reportId: Long
) {

    fun create(): ReportViewerStore {
        return ReportViewerStore(
            implementation = storeFactory.create(
                name = STORE_NAME,
                initialState = ReportViewerStore.State.Loading,
                bootstrapper = ReportViewerBootstrapper(reportId),
                executorFactory = { ReportViewerExecutor(reportId) },
                reducer = ReportViewerReducer(),
            )
        )
    }

    companion object {
        const val STORE_NAME = "ReportViewerStore"
    }

}