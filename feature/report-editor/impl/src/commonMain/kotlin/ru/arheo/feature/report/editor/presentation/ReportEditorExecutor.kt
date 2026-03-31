package ru.arheo.feature.report.editor.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.arheo.core.domain.model.ReportData
import ru.arheo.feature.report.editor.data.mappers.DomainReportMapper
import ru.arheo.feature.report.editor.domian.FileRepository
import ru.arheo.feature.report.editor.domian.ReportRepository
import ru.arheo.feature.report.editor.domian.models.Author
import ru.arheo.feature.report.editor.domian.models.District
import ru.arheo.feature.report.editor.domian.models.Keyword
import ru.arheo.feature.report.editor.domian.models.monument.Monument
import ru.arheo.feature.report.editor.domian.models.report.Report
import ru.arheo.feature.report.editor.domian.models.report.ReportId
import ru.arheo.feature.report.editor.domian.models.report.ReportName
import ru.arheo.feature.report.editor.domian.models.report.ReportWorkType
import ru.arheo.feature.report.editor.domian.models.report.ReportYear
import ru.arheo.feature.report.editor.presentation.models.UiMonument

internal class ReportEditorExecutor(
    private val reportRepository: ReportRepository,
    private val fileRepository: FileRepository
) : CoroutineExecutor<ReportEditorStore.Intent, ReportEditorAction, ReportEditorStore.State, ReportEditorPatch, ReportEditorStore.Label>() {

    private var originalArchivePath: String? = null

    override fun executeAction(action: ReportEditorAction) {
        when (action) {
            is ReportEditorAction.ReportLoaded -> {
                originalArchivePath = action.report.archiveFilePath
                dispatch(ReportEditorPatch.ReportLoaded(action.report))
            }
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
        val content = state() as? ReportEditorStore.State.Content ?: return
        val yearInt = content.year.toIntOrNull()
        if (content.name.isBlank()) {
            dispatch(ReportEditorPatch.SaveError("Название отчёта обязательно"))
            return
        }
        if (yearInt == null) {
            dispatch(ReportEditorPatch.SaveError("Укажите корректный год"))
            return
        }
        if (content.authors.isEmpty()) {
            dispatch(ReportEditorPatch.SaveError("Укажите хотя бы одного автора"))
            return
        }
        dispatch(ReportEditorPatch.Saving)
        scope.launch {
            try {
                val report = buildDomainReport(content, yearInt)
                val reportData = DomainReportMapper(report).toReportData()
                val archivePath = archiveIfNeeded(workingDirectory, hasFiles, reportData)
                val reportWithArchive = report.copy(archiveFilePath = archivePath)
                if (content.isEditing) {
                    deleteStaleArchive(archivePath)
                    reportRepository.updateReport(reportWithArchive)
                } else {
                    reportRepository.addReport(reportWithArchive)
                }
                if (workingDirectory.isNotEmpty()) {
                    fileRepository.cleanupWorkingDirectory(workingDirectory)
                }
                dispatch(ReportEditorPatch.Saved)
                publish(ReportEditorStore.Label.Saved)
            } catch (e: Exception) {
                dispatch(ReportEditorPatch.SaveError(e.message ?: "Ошибка сохранения"))
            }
        }
    }

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

    private fun buildDomainReport(
        state: ReportEditorStore.State.Content,
        year: Int
    ): Report = Report(
        id = state.reportId?.let { ReportId(it) },
        name = ReportName(state.name.trim()),
        year = ReportYear(year),
        authors = state.authors.map { Author(it) },
        workType = ReportWorkType(state.workType.trim()),
        districts = state.districts.map { District(it) },
        keywords = state.keywords.map { Keyword(it) },
        monuments = state.monuments.map { it.toDomain() },
    )

    private fun UiMonument.toDomain(): Monument = Monument(
        id = id,
        name = name,
        type = type,
        culture = culture,
        period = period,
        geographicLocation = geographicLocation,
        number = number,
    )
}
