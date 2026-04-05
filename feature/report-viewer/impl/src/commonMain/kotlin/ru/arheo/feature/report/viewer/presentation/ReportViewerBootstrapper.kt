package ru.arheo.feature.report.viewer.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.launch
import ru.arheo.feature.report.viewer.domain.ReportRepository
import ru.arheo.feature.report.viewer.domain.models.ReportId

internal class ReportViewerBootstrapper(
    private val reportId: ReportId,
    private val reportRepository: ReportRepository
) : CoroutineBootstrapper<ReportViewerAction>() {
    override fun invoke() {
        scope.launch {
            val report = reportRepository.getReportById(reportId) ?: run {
                dispatch(ReportViewerAction.ReportLoadException)
                return@launch
            }
            dispatch(ReportViewerAction.ReportLoaded(report))
        }
    }
}