package ru.arheo.feature.report_editor.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.arheo.core.data.FileManager
import ru.arheo.core.data.ReportRepository
import ru.arheo.core.domain.model.Monument
import ru.arheo.core.domain.model.Report

internal class ReportEditorStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ReportRepository,
    private val fileManager: FileManager,
) {

    fun create(reportId: Long?): ReportEditorStore {
        return object : ReportEditorStore,
            Store<ReportEditorStore.Intent, ReportEditorStore.State, ReportEditorStore.Label> by storeFactory.create(
                name = "ReportEditorStore",
                initialState = ReportEditorStore.State(
                    reportId = reportId,
                    isLoading = reportId != null,
                ),
                bootstrapper = BootstrapperImpl(reportId),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}
    }

    private sealed interface Action {
        data class ReportLoaded(val report: Report) : Action
        data class SuggestionsLoaded(val authors: List<String>, val workTypes: List<String>) : Action
    }

    private sealed interface Msg {
        data class ReportLoaded(val report: Report) : Msg
        data class SuggestionsLoaded(val authors: List<String>, val workTypes: List<String>) : Msg
        data class TitleChanged(val title: String) : Msg
        data class YearChanged(val year: String) : Msg
        data class AuthorsChanged(val authors: String) : Msg
        data class WorkTypeChanged(val workType: String) : Msg
        data class DistrictsChanged(val districts: String) : Msg
        data class KeywordsChanged(val keywords: String) : Msg
        data class MonumentUpdated(val index: Int, val monument: Monument) : Msg
        data object MonumentAdded : Msg
        data class MonumentRemoved(val index: Int) : Msg
        data object Saving : Msg
        data class Error(val message: String) : Msg
        data object Saved : Msg
    }

    private inner class BootstrapperImpl(
        private val reportId: Long?,
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                val authors = repository.getAllAuthors()
                val workTypes = repository.getAllWorkTypes()
                dispatch(Action.SuggestionsLoaded(authors, workTypes))
            }
            if (reportId != null) {
                scope.launch {
                    val report = repository.getReportById(reportId)
                    if (report != null) {
                        dispatch(Action.ReportLoaded(report))
                    }
                }
            }
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ReportEditorStore.Intent, Action, ReportEditorStore.State, Msg, ReportEditorStore.Label>() {

        private var originalArchivePath: String? = null

        override fun executeAction(action: Action) {
            when (action) {
                is Action.ReportLoaded -> {
                    originalArchivePath = action.report.archiveFilePath
                    dispatch(Msg.ReportLoaded(action.report))
                    publish(ReportEditorStore.Label.ArchivePathLoaded(action.report.archiveFilePath))
                }
                is Action.SuggestionsLoaded -> dispatch(Msg.SuggestionsLoaded(action.authors, action.workTypes))
            }
        }

        override fun executeIntent(intent: ReportEditorStore.Intent) {
            when (intent) {
                is ReportEditorStore.Intent.UpdateTitle -> dispatch(Msg.TitleChanged(intent.title))
                is ReportEditorStore.Intent.UpdateYear -> dispatch(Msg.YearChanged(intent.year))
                is ReportEditorStore.Intent.UpdateAuthors -> dispatch(Msg.AuthorsChanged(intent.authors))
                is ReportEditorStore.Intent.UpdateWorkType -> dispatch(Msg.WorkTypeChanged(intent.workType))
                is ReportEditorStore.Intent.UpdateDistricts -> dispatch(Msg.DistrictsChanged(intent.districts))
                is ReportEditorStore.Intent.UpdateKeywords -> dispatch(Msg.KeywordsChanged(intent.keywords))
                is ReportEditorStore.Intent.UpdateMonument -> dispatch(Msg.MonumentUpdated(intent.index, intent.monument))
                is ReportEditorStore.Intent.AddMonument -> dispatch(Msg.MonumentAdded)
                is ReportEditorStore.Intent.RemoveMonument -> dispatch(Msg.MonumentRemoved(intent.index))
                is ReportEditorStore.Intent.Save -> handleSave(intent.workingDirectory, intent.hasFiles)
            }
        }

        private fun handleSave(workingDirectory: String, hasFiles: Boolean) {
            val currentState = state()
            val yearInt = currentState.year.toIntOrNull()
            if (currentState.title.isBlank()) {
                dispatch(Msg.Error("Название отчёта обязательно"))
                return
            }
            if (yearInt == null) {
                dispatch(Msg.Error("Укажите корректный год"))
                return
            }
            if (currentState.authors.isBlank()) {
                dispatch(Msg.Error("Укажите хотя бы одного автора"))
                return
            }
            dispatch(Msg.Saving)
            scope.launch {
                val report = buildReport(currentState, yearInt)
                val archivePath = archiveIfNeeded(workingDirectory, hasFiles, report)
                deleteStaleArchive(archivePath)
                val savedReport = report.copy(archiveFilePath = archivePath)
                if (currentState.isEditing) {
                    repository.updateReport(savedReport)
                } else {
                    repository.addReport(savedReport)
                }
                fileManager.cleanupWorkingDirectory(workingDirectory)
                dispatch(Msg.Saved)
                publish(ReportEditorStore.Label.Saved)
            }
        }

        private fun buildReport(state: ReportEditorStore.State, year: Int): Report =
            Report(
                id = state.reportId ?: 0L,
                title = state.title.trim(),
                year = year,
                authors = state.authors.split(",").map { it.trim() }.filter { it.isNotBlank() },
                workType = state.workType.trim(),
                districts = state.districts.split(",").map { it.trim() }.filter { it.isNotBlank() },
                keywords = state.keywords.split(",").map { it.trim() }.filter { it.isNotBlank() },
                monuments = state.monuments.filter { it.name.isNotBlank() },
            )

        private suspend fun archiveIfNeeded(
            workingDirectory: String,
            hasFiles: Boolean,
            report: Report,
        ): String? {
            if (!hasFiles) return null
            val archiveName = fileManager.computeArchiveName(report)
            return fileManager.archiveWorkingDirectory(workingDirectory, archiveName)
        }

        private suspend fun deleteStaleArchive(newArchivePath: String?) {
            val oldPath = originalArchivePath ?: return
            if (oldPath != newArchivePath) {
                fileManager.deleteArchive(oldPath)
            }
        }
    }

    private object ReducerImpl : Reducer<ReportEditorStore.State, Msg> {
        override fun ReportEditorStore.State.reduce(msg: Msg): ReportEditorStore.State =
            when (msg) {
                is Msg.ReportLoaded -> copy(
                    title = msg.report.title,
                    year = msg.report.year.toString(),
                    authors = msg.report.authors.joinToString(", "),
                    workType = msg.report.workType,
                    districts = msg.report.districts.joinToString(", "),
                    keywords = msg.report.keywords.joinToString(", "),
                    monuments = msg.report.monuments,
                    isLoading = false,
                )
                is Msg.SuggestionsLoaded -> copy(
                    authorSuggestions = msg.authors,
                    workTypeSuggestions = msg.workTypes,
                )
                is Msg.TitleChanged -> copy(title = msg.title, error = null)
                is Msg.YearChanged -> copy(year = msg.year, error = null)
                is Msg.AuthorsChanged -> copy(authors = msg.authors, error = null)
                is Msg.WorkTypeChanged -> copy(workType = msg.workType, error = null)
                is Msg.DistrictsChanged -> copy(districts = msg.districts, error = null)
                is Msg.KeywordsChanged -> copy(keywords = msg.keywords, error = null)
                is Msg.MonumentUpdated -> copy(
                    monuments = monuments.toMutableList().apply { set(msg.index, msg.monument) },
                )
                is Msg.MonumentAdded -> copy(monuments = monuments + Monument())
                is Msg.MonumentRemoved -> copy(
                    monuments = monuments.toMutableList().apply { removeAt(msg.index) },
                )
                is Msg.Saving -> copy(isSaving = true, error = null)
                is Msg.Error -> copy(error = msg.message, isSaving = false)
                is Msg.Saved -> copy(isSaving = false)
            }
    }
}
