package ru.arheo.feature.report.selector.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.arheo.feature.report.selector.domain.FileRepository

internal class ReportSelectorExecutor(
    private val fileRepository: FileRepository
) : CoroutineExecutor<ReportSelectorStore.Intent, ReportSelectorAction, ReportSelectorStore.State, ReportSelectorPatch, Nothing>() {

    override fun executeAction(action: ReportSelectorAction) {
        when (action) {
            is ReportSelectorAction.WorkingDirectoryCreated ->
                dispatch(ReportSelectorPatch.WorkingDirectorySet(action.path))
            is ReportSelectorAction.ArchiveExtracted ->
                dispatch(ReportSelectorPatch.FilesUpdated(action.files))
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
        val content = state() as? ReportSelectorStore.State.Content ?: return
        val dir = content.workingDirectory.ifEmpty { return }
        scope.launch {
            fileRepository.copyToWorking(dir, paths)
            val files = fileRepository.listWorkingFiles(dir)
            dispatch(ReportSelectorPatch.FilesUpdated(files))
        }
    }

    private fun handleRemoveFile(fileName: String) {
        val content = state() as? ReportSelectorStore.State.Content ?: return
        val dir = content.workingDirectory.ifEmpty { return }
        scope.launch {
            fileRepository.removeFromWorking(dir, fileName)
            val files = fileRepository.listWorkingFiles(dir)
            dispatch(ReportSelectorPatch.FilesUpdated(files))
        }
    }

    private fun handleLoadArchive(archivePath: String) {
        val content = state() as? ReportSelectorStore.State.Content ?: return
        val dir = content.workingDirectory.ifEmpty { return }
        dispatch(ReportSelectorPatch.ShowLoading)
        scope.launch {
            fileRepository.extractArchive(archivePath, dir)
            val files = fileRepository.listWorkingFiles(dir)
            dispatch(ReportSelectorPatch.WorkingDirectorySet(dir))
            dispatch(ReportSelectorPatch.FilesUpdated(files))
        }
    }
}
