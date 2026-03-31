package ru.arheo.feature.report.editor.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import ru.arheo.core.domain.model.ReportData
import ru.arheo.feature.report.editor.domian.FileRepository
import ru.arheo.feature.report.editor.domian.ReportRepository

internal class ReportEditorExecutor(
    private val reportRepository: ReportRepository,
    private val fileRepository: FileRepository
) : CoroutineExecutor<ReportEditorStore.Intent, ReportEditorAction, ReportEditorStore.State, ReportEditorPatch, ReportEditorStore.Label>() {

    private var originalArchivePath: String? = null

    override fun executeAction(action: ReportEditorAction) {
        when (action) {
            is ReportEditorAction.ReportLoaded ->
                dispatch(ReportEditorPatch.ReportLoaded(action.report))
            ReportEditorAction.ReportLoadError ->
                dispatch(ReportEditorPatch.ReportLoadError)
        }
    }

    override fun executeIntent(intent: ReportEditorStore.Intent) {
        when (intent) {
            is ReportEditorStore.Intent.UpdateName ->
                dispatch(ReportEditorPatch.NameChanged(intent.name))
            is ReportEditorStore.Intent.UpdateYear ->
                dispatch(ReportEditorPatch.YearChanged(intent.year))
            is ReportEditorStore.Intent.UpdateWorkType ->
                dispatch(ReportEditorPatch.WorkTypeChanged(intent.workType))
            is ReportEditorStore.Intent.AddAuthor ->
                dispatch(ReportEditorPatch.AuthorAdded(intent.author))
            is ReportEditorStore.Intent.RemoveAuthor ->
                dispatch(ReportEditorPatch.AuthorRemoved(intent.author))
            is ReportEditorStore.Intent.AddDistrict ->
                dispatch(ReportEditorPatch.DistrictAdded(intent.district))
            is ReportEditorStore.Intent.RemoveDistrict ->
                dispatch(ReportEditorPatch.DistrictRemoved(intent.district))
            is ReportEditorStore.Intent.AddKeyword ->
                dispatch(ReportEditorPatch.KeywordAdded(intent.keyword))
            is ReportEditorStore.Intent.RemoveKeyword ->
                dispatch(ReportEditorPatch.KeywordRemoved(intent.keyword))
            is ReportEditorStore.Intent.UpdateMonument ->
                dispatch(ReportEditorPatch.MonumentUpdated(intent.index, intent.monument))
            is ReportEditorStore.Intent.AddMonument ->
                dispatch(ReportEditorPatch.MonumentAdded)
            is ReportEditorStore.Intent.RemoveMonument ->
                dispatch(ReportEditorPatch.MonumentRemoved(intent.index))
            is ReportEditorStore.Intent.Save ->
                handleSave(intent.workingDirectory, intent.hasFiles)
        }
    }

    private fun handleSave(workingDirectory: String, hasFiles: Boolean) {
        val currentState = state()
    }

    private fun buildReport(state: ReportEditorStore.State, year: Int): ReportData =
        ReportData()

    private suspend fun archiveIfNeeded(
        workingDirectory: String,
        hasFiles: Boolean,
        report: ReportData,
    ): String? {
        if (!hasFiles) return null
        val archiveName = fileRepository.computeArchiveName(report)
        return fileRepository.archiveWorkingDirectory(workingDirectory, archiveName)
    }

    private suspend fun deleteStaleArchive(newArchivePath: String?) {
        val oldPath = originalArchivePath ?: return
        if (oldPath != newArchivePath) {
            fileRepository.deleteArchive(oldPath)
        }
    }
}
