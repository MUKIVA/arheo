package ru.arheo.feature.report.selector.presentation

import ru.arheo.feature.report.selector.domain.models.FileInfo
import java.nio.file.Path

internal sealed interface ReportSelectorAction {
    data class WorkingDirectoryCreated(val path: Path) : ReportSelectorAction
    data class ArchiveExtracted(val files: List<FileInfo>) : ReportSelectorAction
}
