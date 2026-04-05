package ru.arheo.feature.report.viewer.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.arheo.feature.report.viewer.domain.ReportRepository
import ru.arheo.feature.report.viewer.domain.models.report.ReportId

internal class ReportViewerExecutor(
    private val reportId: ReportId,
    private val reportRepository: ReportRepository
) : CoroutineExecutor<
    ReportViewerStore.Intent,
    ReportViewerAction,
    ReportViewerStore.State,
    ReportViewerPatch,
    ReportViewerStore.Label
>() {

    override fun executeAction(action: ReportViewerAction) {
        when (action) {
            is ReportViewerAction.ReportLoaded ->
                dispatch(ReportViewerPatch.ReportWasLoaded(action.report))

            is ReportViewerAction.ReportLoadException ->
                dispatch(ReportViewerPatch.FailToLoadReport)
        }
    }

    override fun executeIntent(intent: ReportViewerStore.Intent) {
        when (intent) {
            ReportViewerStore.Intent.Loading ->
                dispatch(ReportViewerPatch.Loading)

            ReportViewerStore.Intent.Refresh -> handleRefresh()
        }
    }

    private fun handleRefresh() {
        scope.launch {
            val report = reportRepository.getReportById(reportId) ?: run {
                dispatch(ReportViewerPatch.FailToLoadReport)
                return@launch
            }
            dispatch(ReportViewerPatch.ReportWasLoaded(report))
        }
    }
}