package ru.arheo.feature.report.editor.presentation

import com.arkivanov.mvikotlin.core.store.StoreFactory
import ru.arheo.feature.report.editor.domian.FileRepository
import ru.arheo.feature.report.editor.domian.ReportRepository

internal class ReportEditorStoreFactory(
    private val storeFactory: StoreFactory,
    private val reportRepository: ReportRepository,
    private val fileRepository: FileRepository,
) {

    fun create(reportId: Long?): ReportEditorStore {
        return ReportEditorStore(
            implementation = storeFactory.create(
                name = "ReportEditorStore",
                initialState = ReportEditorStore.State.Loading,
                bootstrapper = ReportEditorBootstrapper(reportId, reportRepository),
                executorFactory = { ReportEditorExecutor(reportRepository, fileRepository) },
                reducer = ReportEditorReducer(),
            )
        )
    }
}

