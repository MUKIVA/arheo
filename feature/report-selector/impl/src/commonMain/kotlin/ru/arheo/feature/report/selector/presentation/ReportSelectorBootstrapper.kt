package ru.arheo.feature.report.selector.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.launch
import ru.arheo.core.data.FileSource

internal class ReportSelectorBootstrapper(
    private val fileSource: FileSource
) : CoroutineBootstrapper<ReportSelectorAction>() {
    override fun invoke() {
        scope.launch {
            val workingDir = fileSource.createWorkingDirectory()
            dispatch(ReportSelectorAction.WorkingDirectoryCreated(workingDir))
        }
    }
}