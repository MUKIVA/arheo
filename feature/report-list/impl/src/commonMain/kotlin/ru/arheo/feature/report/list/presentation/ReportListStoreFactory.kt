package ru.arheo.feature.report.list.presentation

import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.arheo.feature.report.list.domain.ReportRepository

internal class ReportListStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ReportRepository,
) {

    fun create(): ReportListStore {
        return ReportListStore(
            implementation = storeFactory.create(
                name = STORE_NAME,
                initialState = ReportListStore.State.Loading,
                bootstrapper = ReportListBootstrapper(repository),
                executorFactory = { ReportListExecutor(repository) },
                reducer = ReportListReducer(),
            )
        )
    }

    private companion object {
        const val STORE_NAME = "ReportListStore"
    }
}

