package ru.arheo.feature.report.viewer.presentation

import kotlinx.coroutines.flow.StateFlow

internal interface ReportViewerComponent {

    val state: StateFlow<ReportViewerStore.State>

    fun refresh()
    fun back()

}