package ru.arheo.feature.report.selector.presentation

import com.arkivanov.mvikotlin.core.store.Reducer

internal class ReportSelectorReducer : Reducer<ReportSelectorStore.State, ReportSelectorPatch> {
    override fun ReportSelectorStore.State.reduce(
        msg: ReportSelectorPatch
    ): ReportSelectorStore.State = when (this) {
        is ReportSelectorStore.State.Content ->
            reduce(this, msg)
        is ReportSelectorStore.State.Loading ->
            reduce(this, msg)
    }

    private fun reduce(
        state: ReportSelectorStore.State.Loading,
        patch: ReportSelectorPatch
    ) = when (patch) {
        is ReportSelectorPatch.LoadingChanged -> ReportSelectorStore.State.Content()
        else -> state
    }

    private fun reduce(
        state: ReportSelectorStore.State.Content,
        patch: ReportSelectorPatch
    ) = with(state) {
        when (patch) {
            is ReportSelectorPatch.WorkingDirectorySet ->
                copy(workingDirectory = patch.path)
            is ReportSelectorPatch.FilesUpdated ->
                copy(attachedFiles = patch.files)
            is ReportSelectorPatch.DragOverChanged ->
                copy(isDraggingOver = patch.isDragging)
            is ReportSelectorPatch.LoadingChanged -> if (patch.isLoading) {
                ReportSelectorStore.State.Loading
            } else {
                state
            }
        }
    }


}