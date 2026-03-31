package ru.arheo.feature.report.selector.presentation

import ru.arheo.feature.report.selector.domain.models.FileInfo

internal sealed interface ReportSelectorAction {
    data class WorkingDirectoryCreated(val path: String) : ReportSelectorAction
    data class ArchiveExtracted(val files: List<FileInfo>) : ReportSelectorAction
}
