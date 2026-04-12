package ru.arheo.feature.report.viewer.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.arheo.feature.report.viewer.domain.FileRepository
import ru.arheo.feature.report.viewer.domain.ReportRepository
import ru.arheo.feature.report.viewer.domain.models.report.ReportId
import java.awt.Desktop
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

internal class ReportViewerExecutor(
    private val reportId: ReportId,
    private val reportRepository: ReportRepository,
    private val fileRepository: FileRepository
) : CoroutineExecutor<
    ReportViewerStore.Intent,
    ReportViewerAction,
    ReportViewerStore.State,
    ReportViewerPatch,
    ReportViewerStore.Label
>() {

    private val pathsForClean = mutableListOf<Path>()

    override fun executeAction(action: ReportViewerAction) {
        when (action) {
            is ReportViewerAction.ReportLoaded ->
                dispatch(ReportViewerPatch.ReportWasLoaded(action.report))

            is ReportViewerAction.ReportLoadException ->
                dispatch(ReportViewerPatch.FailToLoadReport)
        }
    }

    override fun executeIntent(intent: ReportViewerStore.Intent) {
        when (intent) {
            ReportViewerStore.Intent.Loading ->
                dispatch(ReportViewerPatch.Loading)

            ReportViewerStore.Intent.Refresh -> handleRefresh()
            ReportViewerStore.Intent.OpenMaterials -> handleOpenMaterials()
        }
    }

    override fun dispose() = runBlocking {
        pathsForClean.onEach { path ->
            fileRepository.cleanupWorkingDirectory(path)
        }
        super.dispose()
    }

    private fun handleRefresh() {
        scope.launch {
            val report = reportRepository.getReportById(reportId) ?: run {
                dispatch(ReportViewerPatch.FailToLoadReport)
                return@launch
            }
            dispatch(ReportViewerPatch.ReportWasLoaded(report))
        }
    }

    private fun handleOpenMaterials() {
        val state = state()

        if (state !is ReportViewerStore.State.Content)
            return

        scope.launch {
            if (state.report.archive == null) {
                return@launch
            }
            val workingDirectory = fileRepository.createWorkingDirectory().apply {
                pathsForClean.add(this)
            }
            fileRepository.extractArchive(workingDirectory, state.report.archive)
            openDirectoryInSystemExplorer(workingDirectory)
        }
    }

    private fun openDirectoryInSystemExplorer(working: Path) {
        if (!working.exists() || !working.isDirectory()) {
            return
        }

        return try {
            if (!Desktop.isDesktopSupported()) { return }
            Desktop.getDesktop().open(working.toFile())
        } catch (_: Throwable) {}
    }
}