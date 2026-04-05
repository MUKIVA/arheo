package ru.arheo.feature.report.viewer.presentation

import ru.arheo.feature.report.viewer.domain.models.report.Report

internal sealed interface ReportViewerAction {
    data class ReportLoaded(
        val report: Report
    ) : ReportViewerAction

    data object ReportLoadException : ReportViewerAction
}