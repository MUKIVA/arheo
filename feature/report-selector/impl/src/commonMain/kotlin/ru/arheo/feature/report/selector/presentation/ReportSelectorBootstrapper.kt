package ru.arheo.feature.report.selector.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.launch
import ru.arheo.feature.report.selector.domain.FileRepository
import java.nio.file.Path

internal class ReportSelectorBootstrapper(
    private val fileRepository: FileRepository,
    private val working: Path,
    private val archive: Path?
) : CoroutineBootstrapper<ReportSelectorAction>() {
    override fun invoke() {
        scope.launch {
            if (archive != null) {
                fileRepository.extractArchive(archive, working)
                val files = fileRepository.listWorkingFiles(working)
                dispatch(ReportSelectorAction.WorkingDirectoryCreated(working))
                dispatch(ReportSelectorAction.ArchiveExtracted(files))
            } else {
                dispatch(ReportSelectorAction.WorkingDirectoryCreated(working))
            }
        }
    }
}
