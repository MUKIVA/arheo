package ru.arheo.feature.report.viewer.presentation

internal sealed interface ReportViewerPatch {

    data class ReportWasLoaded(val reportId: Long) : ReportViewerPatch
    data object FailToLoadReport : ReportViewerPatch
    data object Loading : ReportViewerPatch

}