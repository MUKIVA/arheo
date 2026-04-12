package ru.arheo.feature.report.selector.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import ru.arheo.feature.report.selector.domain.models.FileInfo
import ru.arheo.feature.report.selector.presentation.models.UiFileInfo

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
    ): ReportSelectorStore.State = when (patch) {
        is ReportSelectorPatch.WorkingDirectorySet ->
            ReportSelectorStore.State.Content()
        else -> state
    }

    private fun reduce(
        state: ReportSelectorStore.State.Content,
        patch: ReportSelectorPatch
    ): ReportSelectorStore.State = when (patch) {
        is ReportSelectorPatch.FilesUpdated ->
            state.copy(attachedFiles = patch.files.asUiFileInfoList())
        is ReportSelectorPatch.DragOverChanged ->
            state.copy(isDraggingOver = patch.isDragging)
        is ReportSelectorPatch.ShowLoading ->
            ReportSelectorStore.State.Loading
        else -> state
    }
}

private fun List<FileInfo>.asUiFileInfoList(): List<UiFileInfo> = map { item ->
    UiFileInfo(
        name = item.name.value,
        size = item.size.value,
        isDirectory = item.isDirectory.value,
    )
}
