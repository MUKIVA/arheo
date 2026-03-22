package ru.arheo.feature.report_selector.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import ru.arheo.core.data.FileManager
import ru.arheo.core.domain.model.FileInfo

internal class ReportSelectorStoreFactory(
    private val storeFactory: StoreFactory,
    private val fileManager: FileManager,
) {

    fun create(): ReportSelectorStore {
        return object : ReportSelectorStore,
            Store<ReportSelectorStore.Intent, ReportSelectorStore.State, Nothing> by storeFactory.create(
                name = "ReportSelectorStore",
                initialState = ReportSelectorStore.State(),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}
    }

    private sealed interface Action {
        data class WorkingDirectoryCreated(val path: String) : Action
    }

    private sealed interface Msg {
        data class WorkingDirectorySet(val path: String) : Msg
        data class FilesUpdated(val files: List<FileInfo>) : Msg
        data class DragOverChanged(val isDragging: Boolean) : Msg
        data class LoadingChanged(val isLoading: Boolean) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                val workingDir = fileManager.createWorkingDirectory()
                dispatch(Action.WorkingDirectoryCreated(workingDir))
            }
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ReportSelectorStore.Intent, Action, ReportSelectorStore.State, Msg, Nothing>() {

        override fun executeAction(action: Action) {
            when (action) {
                is Action.WorkingDirectoryCreated -> dispatch(Msg.WorkingDirectorySet(action.path))
            }
        }

        override fun executeIntent(intent: ReportSelectorStore.Intent) {
            when (intent) {
                is ReportSelectorStore.Intent.AttachFiles -> handleAttachFiles(intent.paths)
                is ReportSelectorStore.Intent.RemoveFile -> handleRemoveFile(intent.fileName)
                is ReportSelectorStore.Intent.UpdateDragOver -> dispatch(Msg.DragOverChanged(intent.isDragging))
                is ReportSelectorStore.Intent.LoadArchive -> handleLoadArchive(intent.archivePath)
            }
        }

        private fun handleAttachFiles(paths: List<String>) {
            val dir = state().workingDirectory.ifEmpty { return }
            scope.launch {
                fileManager.copyToWorking(dir, paths)
                val files = fileManager.listWorkingFiles(dir)
                dispatch(Msg.FilesUpdated(files))
            }
        }

        private fun handleRemoveFile(fileName: String) {
            val dir = state().workingDirectory.ifEmpty { return }
            scope.launch {
                fileManager.removeFromWorking(dir, fileName)
                val files = fileManager.listWorkingFiles(dir)
                dispatch(Msg.FilesUpdated(files))
            }
        }

        private fun handleLoadArchive(archivePath: String) {
            val dir = state().workingDirectory.ifEmpty { return }
            dispatch(Msg.LoadingChanged(true))
            scope.launch {
                fileManager.extractArchive(archivePath, dir)
                val files = fileManager.listWorkingFiles(dir)
                dispatch(Msg.FilesUpdated(files))
                dispatch(Msg.LoadingChanged(false))
            }
        }
    }

    private object ReducerImpl : Reducer<ReportSelectorStore.State, Msg> {
        override fun ReportSelectorStore.State.reduce(msg: Msg): ReportSelectorStore.State =
            when (msg) {
                is Msg.WorkingDirectorySet -> copy(workingDirectory = msg.path)
                is Msg.FilesUpdated -> copy(attachedFiles = msg.files)
                is Msg.DragOverChanged -> copy(isDraggingOver = msg.isDragging)
                is Msg.LoadingChanged -> copy(isLoading = msg.isLoading)
            }
    }
}
