package ru.arheo.feature.report.selector.presentation

import kotlinx.coroutines.flow.StateFlow

internal interface ReportSelectorComponent {

    val state: StateFlow<ReportSelectorStore.State>

    fun onAttachFiles(paths: List<String>)
    fun onRemoveFile(fileName: String)
    fun onDragOver(isDragging: Boolean)
    fun loadArchive(archivePath: String)

}
