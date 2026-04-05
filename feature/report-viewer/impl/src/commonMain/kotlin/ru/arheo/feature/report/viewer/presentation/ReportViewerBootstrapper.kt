package ru.arheo.feature.report.viewer.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper

internal class ReportViewerBootstrapper(
    private val reportId: Long
) : CoroutineBootstrapper<ReportViewerAction>() {
    override fun invoke() {
        dispatch(ReportViewerAction.ReportLoaded(reportId))
    }
}