package ru.arheo.feature.report.selector.presentation

import ru.arheo.feature.report.selector.domain.models.FileInfo

internal sealed interface ReportSelectorPatch {
    data class WorkingDirectorySet(val path: String) : ReportSelectorPatch
    data class FilesUpdated(val files: List<FileInfo>) : ReportSelectorPatch
    data class DragOverChanged(val isDragging: Boolean) : ReportSelectorPatch
    data object ShowLoading : ReportSelectorPatch
}
