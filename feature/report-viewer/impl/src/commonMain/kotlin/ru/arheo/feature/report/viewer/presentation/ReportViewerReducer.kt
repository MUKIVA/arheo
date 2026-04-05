package ru.arheo.feature.report.viewer.presentation

import com.arkivanov.mvikotlin.core.store.Reducer

internal class ReportViewerReducer : Reducer<ReportViewerStore.State, ReportViewerPatch> {
    override fun ReportViewerStore.State.reduce(
        msg: ReportViewerPatch
    ): ReportViewerStore.State {
        return when (this) {
            is ReportViewerStore.State.Content ->
                reduce(this, msg)
            is ReportViewerStore.State.Error ->
                reduce(this, msg)
            is ReportViewerStore.State.Loading ->
                reduce(this, msg)
        }
    }

    private fun reduce(
        state: ReportViewerStore.State.Loading,
        patch: ReportViewerPatch
    ): ReportViewerStore.State {
        return when (patch) {
            is ReportViewerPatch.ReportWasLoaded -> ReportViewerStore.State.Content(
                reportId = patch.report.id
            )
            is ReportViewerPatch.FailToLoadReport -> ReportViewerStore.State.Error
            else -> state
        }
    }

    private fun reduce(
        state: ReportViewerStore.State.Error,
        patch: ReportViewerPatch
    ): ReportViewerStore.State {
        return when (patch) {
            is ReportViewerPatch.Loading -> ReportViewerStore.State.Loading
            else -> state
        }
    }

    private fun reduce(
        state: ReportViewerStore.State.Content,
        patch: ReportViewerPatch
    ): ReportViewerStore.State {
        return when (patch) {
            is ReportViewerPatch.Loading -> ReportViewerStore.State.Loading
            else -> state
        }
    }

}