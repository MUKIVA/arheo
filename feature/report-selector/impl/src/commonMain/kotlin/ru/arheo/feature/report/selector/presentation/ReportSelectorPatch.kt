package ru.arheo.feature.report.selector.presentation

import ru.arheo.core.domain.model.FileInfoData

internal sealed interface ReportSelectorPatch {
    data class WorkingDirectorySet(val path: String) : ReportSelectorPatch
    data class FilesUpdated(val files: List<FileInfoData>) : ReportSelectorPatch
    data class DragOverChanged(val isDragging: Boolean) : ReportSelectorPatch
    data class LoadingChanged(val isLoading: Boolean) : ReportSelectorPatch
}