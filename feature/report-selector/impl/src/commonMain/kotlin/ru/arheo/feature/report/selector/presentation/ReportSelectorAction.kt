package ru.arheo.feature.report.selector.presentation

internal sealed interface ReportSelectorAction {
    data class WorkingDirectoryCreated(val path: String) : ReportSelectorAction
}