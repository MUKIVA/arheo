package ru.arheo.feature.report.selector.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.arheo.core.data.FileSource

internal class ReportSelectorExecutor(
    private val fileSource: FileSource
) : CoroutineExecutor<ReportSelectorStore.Intent, ReportSelectorAction, ReportSelectorStore.State, ReportSelectorPatch, Nothing>() {

    override fun executeAction(action: ReportSelectorAction) {
        when (action) {
            is ReportSelectorAction.WorkingDirectoryCreated -> dispatch(ReportSelectorPatch.WorkingDirectorySet(action.path))
        }
    }

    override fun executeIntent(intent: ReportSelectorStore.Intent) {
        when (intent) {
            is ReportSelectorStore.Intent.AttachFiles -> handleAttachFiles(intent.paths)
            is ReportSelectorStore.Intent.RemoveFile -> handleRemoveFile(intent.fileName)
            is ReportSelectorStore.Intent.UpdateDragOver -> dispatch(ReportSelectorPatch.DragOverChanged(intent.isDragging))
            is ReportSelectorStore.Intent.LoadArchive -> handleLoadArchive(intent.archivePath)
        }
    }

    private fun handleAttachFiles(paths: List<String>) {
        val dir = state().workingDirectory.ifEmpty { return }
        scope.launch {
            fileSource.copyToWorking(dir, paths)
            val files = fileSource.listWorkingFiles(dir)
            dispatch(ReportSelectorPatch.FilesUpdated(files))
        }
    }

    private fun handleRemoveFile(fileName: String) {
        val dir = state().workingDirectory.ifEmpty { return }
        scope.launch {
            fileSource.removeFromWorking(dir, fileName)
            val files = fileSource.listWorkingFiles(dir)
            dispatch(ReportSelectorPatch.FilesUpdated(files))
        }
    }

    private fun handleLoadArchive(archivePath: String) {
        val dir = state().workingDirectory.ifEmpty { return }
        dispatch(ReportSelectorPatch.LoadingChanged(true))
        scope.launch {
            fileSource.extractArchive(archivePath, dir)
            val files = fileSource.listWorkingFiles(dir)
            dispatch(ReportSelectorPatch.FilesUpdated(files))
            dispatch(ReportSelectorPatch.LoadingChanged(false))
        }
    }
}