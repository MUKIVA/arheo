package ru.arheo.feature.report.viewer.presentation

import ru.arheo.feature.report.viewer.domain.models.report.Report

internal sealed interface ReportViewerPatch {

    data class ReportWasLoaded(val report: Report) : ReportViewerPatch
    data object FailToLoadReport : ReportViewerPatch
    data object Loading : ReportViewerPatch

}