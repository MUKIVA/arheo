package ru.arheo.feature.report.selector.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.launch
import ru.arheo.feature.report.selector.domain.FileRepository

internal class ReportSelectorBootstrapper(
    private val fileRepository: FileRepository
) : CoroutineBootstrapper<ReportSelectorAction>() {
    override fun invoke() {
        scope.launch {
            val workingDir = fileRepository.createWorkingDirectory()
            dispatch(ReportSelectorAction.WorkingDirectoryCreated(workingDir))
        }
    }
}
