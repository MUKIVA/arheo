package ru.arheo.feature.report.selector.presentation

import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.arheo.feature.report.selector.domain.FileRepository

internal class ReportSelectorStoreFactory(
    private val storeFactory: StoreFactory,
    private val fileRepository: FileRepository,
) {

    fun create(): ReportSelectorStore {
        return ReportSelectorStore(
            implementation = storeFactory.create(
                name = STORE_NAME,
                initialState = ReportSelectorStore.State.Loading,
                bootstrapper = ReportSelectorBootstrapper(fileRepository),
                executorFactory = { ReportSelectorExecutor(fileRepository) },
                reducer = ReportSelectorReducer(),
            )
        )
    }

    private companion object {
        const val STORE_NAME = "ReportSelectorStore"
    }
}
