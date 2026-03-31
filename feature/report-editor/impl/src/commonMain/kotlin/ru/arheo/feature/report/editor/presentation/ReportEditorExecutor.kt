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
            is ReportEditorAction.ReportLoaded -> {
                dispatch(ReportEditorPatch.ReportLoaded(action.report))
//                originalArchivePath = action.report.archiveFilePath
//                dispatch(ReportEditorPatch.ReportLoaded(action.report))
//                publish(ReportEditorStore.Label.ArchivePathLoaded(action.report.archiveFilePath))
            }
            ReportEditorAction.ReportLoadError -> dispatch(ReportEditorPatch.ReportLoadError)
        }
    }

    override fun executeIntent(intent: ReportEditorStore.Intent) {
        when (intent) {
            is ReportEditorStore.Intent.UpdateName ->
                dispatch(ReportEditorPatch.NameChanged(intent.name))
            is ReportEditorStore.Intent.UpdateYear ->
                dispatch(ReportEditorPatch.YearChanged(intent.year))
            is ReportEditorStore.Intent.UpdateAuthors ->
                dispatch(ReportEditorPatch.AuthorsChanged(intent.authors))
            is ReportEditorStore.Intent.UpdateWorkType ->
                dispatch(ReportEditorPatch.WorkTypeChanged(intent.workType))
            is ReportEditorStore.Intent.UpdateDistricts ->
                dispatch(ReportEditorPatch.DistrictsChanged(intent.districts))
            is ReportEditorStore.Intent.UpdateKeywords ->
                dispatch(ReportEditorPatch.KeywordsChanged(intent.keywords))
            is ReportEditorStore.Intent.UpdateMonument ->
                dispatch(ReportEditorPatch.MonumentUpdated(intent.index, intent.monument))
            is ReportEditorStore.Intent.AddMonument ->
                dispatch(ReportEditorPatch.MonumentAdded)
            is ReportEditorStore.Intent.RemoveMonument ->
                dispatch(ReportEditorPatch.MonumentRemoved(intent.index))
            is ReportEditorStore.Intent.Save -> handleSave(intent.workingDirectory, intent.hasFiles)
        }
    }

    private fun handleSave(workingDirectory: String, hasFiles: Boolean) {
        val currentState = state()
//        val yearInt = currentState.year.toIntOrNull()
//        if (currentState.name.isBlank()) {
//            dispatch(ReportEditorPatch.Error("Название отчёта обязательно"))
//            return
//        }
//        if (yearInt == null) {
//            dispatch(ReportEditorPatch.Error("Укажите корректный год"))
//            return
//        }
//        if (currentState.authors.isBlank()) {
//            dispatch(ReportEditorPatch.Error("Укажите хотя бы одного автора"))
//            return
//        }
//        dispatch(ReportEditorPatch.Saving)
//        scope.launch {
//            val report = buildReport(currentState, yearInt)
//            val archivePath = archiveIfNeeded(workingDirectory, hasFiles, report)
//            deleteStaleArchive(archivePath)
//            val savedReport = report.copy(archiveFilePath = archivePath)
//            if (currentState.isEditing) {
//                reportRepository.updateReport(savedReport)
//            } else {
//                reportRepository.addReport(savedReport)
//            }
//            fileRepository.cleanupWorkingDirectory(workingDirectory)
//            dispatch(ReportEditorPatch.Saved)
//            publish(ReportEditorStore.Label.Saved)
//        }
    }

    private fun buildReport(state: ReportEditorStore.State, year: Int): ReportData =
        ReportData(
//            id = state.reportId ?: 0L,
//            title = state.title.trim(),
//            year = year,
//            authors = state.authors.split(",").map { it.trim() }.filter { it.isNotBlank() },
//            workType = state.workType.trim(),
//            districts = state.districts.split(",").map { it.trim() }.filter { it.isNotBlank() },
//            keywords = state.keywords.split(",").map { it.trim() }.filter { it.isNotBlank() },
//            monuments = state.monuments.filter { it.name.isNotBlank() },
        )

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