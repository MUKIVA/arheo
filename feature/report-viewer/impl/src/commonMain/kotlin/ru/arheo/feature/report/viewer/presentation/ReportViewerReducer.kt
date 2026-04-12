package ru.arheo.feature.report.viewer.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import ru.arheo.feature.report.viewer.presentation.mappers.MonumentMapper
import ru.arheo.feature.report.viewer.presentation.mappers.UiMapper
import ru.arheo.feature.report.viewer.presentation.models.UiMonument

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
                report = UiMapper.from(patch.report).toUi(),
                monuments = listOf(UiMonument.Header) + patch.report.monuments
                    .map(UiMapper::from)
                    .map(MonumentMapper::toUiContent),
                hasAttachedFiles = patch.report.archive != null
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