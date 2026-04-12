package ru.arheo.feature.report.editor.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.arheo.feature.report.editor.domian.FileRepository
import ru.arheo.feature.report.editor.domian.ReportRepository
import ru.arheo.feature.report.editor.domian.models.report.Report
import ru.arheo.feature.report.editor.domian.models.report.ReportId
import java.nio.file.Path

internal class ReportEditorBootstrapper(
    private val reportId: Long?,
    private val reportRepository: ReportRepository,
    private val fileRepository: FileRepository
) : CoroutineBootstrapper<ReportEditorAction>() {

    private val pathsForClean = mutableListOf<Path>()

    override fun invoke() {
        scope.launch {
            val working = fileRepository.createWorkingDirectory().apply {
                pathsForClean.add(this)
            }

            when {
                reportId != null -> editReport(reportId, working)
                else -> createNewReport(working)
            }
        }
    }

    override fun dispose() = runBlocking {
        pathsForClean.onEach { path ->
            fileRepository.cleanupWorkingDirectory(path)
        }
        super.dispose()
    }

    private fun createNewReport(working: Path) {
        dispatch(ReportEditorAction.ReportLoaded(Report.default(), working))
    }

    private fun editReport(reportId: Long, working: Path) {
        scope.launch {
            val report = reportRepository.getReportById(ReportId(reportId)) ?: run {
                dispatch(ReportEditorAction.ReportLoadError)
                return@launch
            }
            dispatch(ReportEditorAction.ReportLoaded(report, working))
        }
    }
}