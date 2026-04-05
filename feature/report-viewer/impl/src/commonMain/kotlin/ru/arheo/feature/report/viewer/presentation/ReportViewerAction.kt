package ru.arheo.feature.report.viewer.presentation

internal sealed interface ReportViewerAction {
    data class ReportLoaded(
        val reportId: Long
    ) : ReportViewerAction

    data object ReportLoadException : ReportViewerAction
}