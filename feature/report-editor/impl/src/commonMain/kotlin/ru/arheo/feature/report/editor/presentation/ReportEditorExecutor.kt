package ru.arheo.feature.report.editor.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
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
import ru.arheo.feature.report.editor.presentation.models.SaveValidationError
import ru.arheo.feature.report.editor.presentation.models.UiMonument
import java.nio.file.Files

internal class ReportEditorExecutor(
    private val reportRepository: ReportRepository,
    private val fileRepository: FileRepository
) : CoroutineExecutor<ReportEditorStore.Intent, ReportEditorAction, ReportEditorStore.State, ReportEditorPatch, ReportEditorStore.Label>() {

    override fun executeAction(action: ReportEditorAction) {
        when (action) {
            is ReportEditorAction.ReportLoaded -> {
                dispatch(ReportEditorPatch.ReportLoaded(action.report, action.working))
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
                handleSave()
        }
    }

    private fun handleSave() {
        val content = state() as? ReportEditorStore.State.Content ?: return
        val yearInt = content.year.toIntOrNull()
        if (content.name.isBlank()) {
            publish(ReportEditorStore.Label.SaveError(SaveValidationError.EMPTY_TITLE))
            return
        }
        if (yearInt == null) {
            publish(ReportEditorStore.Label.SaveError(SaveValidationError.INVALID_YEAR))
            return
        }
        if (content.authors.isEmpty()) {
            publish(ReportEditorStore.Label.SaveError(SaveValidationError.NO_AUTHORS))
            return
        }

        dispatch(ReportEditorPatch.Saving)


        scope.launch {
            try {
                val report = buildDomainReport(content, yearInt)
                val archiveName = fileRepository.computeArchiveName(report)

                val archivePath = when {
                    Files.walk(content.woking).toList().isNotEmpty() -> fileRepository
                        .archiveWorkingDirectory(content.woking, archiveName)
                    else -> null
                }

                val reportWithArchive = report.copy(archive = archivePath)
                if (content.isEditing) {
                    reportRepository.updateReport(reportWithArchive)
                } else {
                    reportRepository.addReport(reportWithArchive)
                }

                publish(ReportEditorStore.Label.Saved)
            } catch (_: Exception) {
                publish(ReportEditorStore.Label.SaveError(SaveValidationError.SAVE_FAILED))
            } finally {
                dispatch(ReportEditorPatch.SaveFinished)
            }
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
