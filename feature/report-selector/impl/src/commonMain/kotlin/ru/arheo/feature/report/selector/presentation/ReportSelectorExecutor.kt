package ru.arheo.feature.report.selector.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.arheo.feature.report.selector.domain.FileRepository
import java.nio.file.Path

internal class ReportSelectorExecutor(
    private val fileRepository: FileRepository
) : CoroutineExecutor<ReportSelectorStore.Intent, ReportSelectorAction, ReportSelectorStore.State, ReportSelectorPatch, Nothing>() {

    private lateinit var workingDirectory: Path

    override fun executeAction(action: ReportSelectorAction) {
        when (action) {
            is ReportSelectorAction.WorkingDirectoryCreated -> {
                workingDirectory = action.path
                dispatch(ReportSelectorPatch.WorkingDirectorySet)
            }
            is ReportSelectorAction.ArchiveExtracted ->
                dispatch(ReportSelectorPatch.FilesUpdated(action.files))
        }
    }

    override fun executeIntent(intent: ReportSelectorStore.Intent) {
        when (intent) {
            is ReportSelectorStore.Intent.AttachFiles ->
                handleAttachFiles(intent.paths)
            is ReportSelectorStore.Intent.RemoveFile ->
                handleRemoveFile(intent.fileName)
            is ReportSelectorStore.Intent.UpdateDragOver ->
                dispatch(ReportSelectorPatch.DragOverChanged(intent.isDragging))
            is ReportSelectorStore.Intent.LoadArchive ->
                handleLoadArchive(intent.archivePath)
        }
    }

    private fun handleAttachFiles(paths: List<Path>) = scope.launch {
        fileRepository.copyToWorking(workingDirectory, paths)
        val files = fileRepository.listWorkingFiles(workingDirectory)
        dispatch(ReportSelectorPatch.FilesUpdated(files))
    }


    private fun handleRemoveFile(fileName: String) = scope.launch {
        fileRepository.removeFromWorking(workingDirectory, fileName)
        val files = fileRepository.listWorkingFiles(workingDirectory)
        dispatch(ReportSelectorPatch.FilesUpdated(files))
    }


    private fun handleLoadArchive(archivePath: Path) = scope.launch {
        dispatch(ReportSelectorPatch.ShowLoading)
        fileRepository.extractArchive(archivePath, workingDirectory)
        val files = fileRepository.listWorkingFiles(workingDirectory)
        dispatch(ReportSelectorPatch.WorkingDirectorySet)
        dispatch(ReportSelectorPatch.FilesUpdated(files))
    }

}
