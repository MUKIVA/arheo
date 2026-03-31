package ru.arheo.feature.report.selector.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.launch
import ru.arheo.feature.report.selector.domain.FileRepository

internal class ReportSelectorBootstrapper(
    private val fileRepository: FileRepository,
    private val archiveFilePath: String?,
) : CoroutineBootstrapper<ReportSelectorAction>() {
    override fun invoke() {
        scope.launch {
            val workingDir = fileRepository.createWorkingDirectory()
            if (!archiveFilePath.isNullOrEmpty()) {
                fileRepository.extractArchive(archiveFilePath, workingDir)
                val files = fileRepository.listWorkingFiles(workingDir)
                dispatch(ReportSelectorAction.WorkingDirectoryCreated(workingDir))
                dispatch(ReportSelectorAction.ArchiveExtracted(files))
            } else {
                dispatch(ReportSelectorAction.WorkingDirectoryCreated(workingDir))
            }
        }
    }
}
