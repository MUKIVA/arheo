package ru.arheo.feature.report.viewer.presentation

import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.arheo.feature.report.viewer.domain.FileRepository
import ru.arheo.feature.report.viewer.domain.ReportRepository
import ru.arheo.feature.report.viewer.domain.models.report.ReportId

internal class ReportViewerStoreFactory(
    private val storeFactory: StoreFactory,
    private val reportId: Long,
    private val reportRepository: ReportRepository,
    private val fileRepository: FileRepository
) {

    fun create(): ReportViewerStore {
        return ReportViewerStore(
            implementation = storeFactory.create(
                name = STORE_NAME,
                initialState = ReportViewerStore.State.Loading,
                bootstrapper = ReportViewerBootstrapper(
                    reportId = ReportId(reportId),
                    reportRepository = reportRepository
                ),
                executorFactory = {
                    ReportViewerExecutor(
                        reportId = ReportId(reportId),
                        reportRepository = reportRepository,
                        fileRepository = fileRepository
                    )
                },
                reducer = ReportViewerReducer(),
            )
        )
    }

    companion object {
        const val STORE_NAME = "ReportViewerStore"
    }

}