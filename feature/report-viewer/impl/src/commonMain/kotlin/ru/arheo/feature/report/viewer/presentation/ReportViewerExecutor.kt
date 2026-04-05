package ru.arheo.feature.report.viewer.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch

internal class ReportViewerExecutor(
    private val reportId: Long
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
                dispatch(ReportViewerPatch.ReportWasLoaded(action.reportId))

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
        dispatch(ReportViewerPatch.ReportWasLoaded(reportId))
    }
}