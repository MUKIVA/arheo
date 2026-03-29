package ru.arheo.feature.report.selector.presentation

import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.arheo.core.data.FileSource

internal class ReportSelectorStoreFactory(
    private val storeFactory: StoreFactory,
    private val fileSource: FileSource,
) {

    fun create(): ReportSelectorStore {
        return ReportSelectorStore(
            implementation = storeFactory.create(
                name = STORE_NAME,
                initialState = ReportSelectorStore.State(),
                bootstrapper = ReportSelectorBootstrapper(fileSource),
                executorFactory = { ReportSelectorExecutor(fileSource) } ,
                reducer = ReportSelectorReducer(),
            )
        )
    }

    private companion object {
        const val STORE_NAME = "ReportSelectorStore"
    }
}

