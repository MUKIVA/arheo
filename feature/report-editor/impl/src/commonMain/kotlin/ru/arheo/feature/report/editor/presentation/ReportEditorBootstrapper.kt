package ru.arheo.feature.report.editor.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.launch
import ru.arheo.feature.report.editor.domian.ReportRepository
import ru.arheo.feature.report.editor.domian.models.report.Report
import ru.arheo.feature.report.editor.domian.models.report.ReportId

internal class ReportEditorBootstrapper(
    private val reportId: Long?,
    private val repository: ReportRepository
) : CoroutineBootstrapper<ReportEditorAction>() {
    override fun invoke() {
//        scope.launch {
//            val authors = repository.getAllAuthors()
//            val workTypes = repository.getAllWorkTypes()
//            dispatch(ReportEditorAction.SuggestionsLoaded(authors, workTypes))
//        }
//        if (reportId != null) {
//            scope.launch {
//                val report = repository.getReportById(ReportId(reportId))
//                if (report != null) {
//                    dispatch(ReportEditorAction.ReportLoaded(report))
//                }
//            }
//        }
        when {
            reportId != null -> editReport(reportId)
            else -> createNewReport()
        }
    }

    private fun createNewReport() {
        dispatch(ReportEditorAction.ReportLoaded(Report.default()))
    }

    private fun editReport(reportId: Long) {
        scope.launch {
            val report = repository.getReportById(ReportId(reportId))
            if (report == null) {
                dispatch(ReportEditorAction.ReportLoadError)
            } else {
                dispatch(ReportEditorAction.ReportLoaded(report))
            }

        }
    }
}