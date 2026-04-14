package ru.arheo.feature.report.selector.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.subscribe
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import ru.arheo.feature.report.selector.presentation.models.UiChooseType
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.io.File
import java.nio.file.Path
import javax.swing.JFileChooser

internal class DefaultReportSelectorComponent(
    componentContext: ComponentContext,
    private val reportSelectorStoreFactory: ReportSelectorStoreFactory
) : ReportSelectorComponent, ComponentContext by componentContext {

    private val chooser by lazy {
        JFileChooser()
    }

    private val store: ReportSelectorStore by lazy {
        reportSelectorStoreFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ReportSelectorStore.State> = store.stateFlow

    init {
        lifecycle.subscribe(
            onDestroy = { store.dispose() }
        )
    }

    override fun onAttachFiles(dialogTitle: String, type: UiChooseType) {
        val paths: List<Path> = when (type) {
            UiChooseType.FILE -> pickFiles(dialogTitle) ?: return
            UiChooseType.DIRECTORY -> {
                val directory: Path = pickDirectory(dialogTitle) ?: return
                listOf(directory)
            }
        }
        store.accept(ReportSelectorStore.Intent.AttachFiles(paths))
    }

    override fun onDrop(transferable: Transferable): Boolean {
        if (!transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
            return false

        val paths = transferable.getTransferData(DataFlavor.javaFileListFlavor)
            .let { it as? List<*> }
            ?.mapNotNull { item -> item as? File }
            ?.map { it.toPath() }
            ?: return false

        store.accept(ReportSelectorStore.Intent.AttachFiles(paths))
        return true
    }

    override fun onRemoveFile(fileName: String) {
        store.accept(ReportSelectorStore.Intent.RemoveFile(fileName))
    }

    override fun onDragOver(isDragging: Boolean) {
        store.accept(ReportSelectorStore.Intent.UpdateDragOver(isDragging))
    }

    override fun loadArchive(archive: Path) {
        store.accept(ReportSelectorStore.Intent.LoadArchive(archive))
    }

    private fun pickFiles(dialogTitle: String): List<Path>? {
        chooser.apply {
            isMultiSelectionEnabled = true
            fileSelectionMode = JFileChooser.FILES_ONLY
            this.dialogTitle = dialogTitle
        }
        return when (chooser.showOpenDialog(null)) {
            JFileChooser.APPROVE_OPTION -> chooser.selectedFiles.map { it.toPath() }
            else -> null
        }
    }

    private fun pickDirectory(dialogTitle: String): Path? {
        chooser.apply {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            this.dialogTitle = dialogTitle
        }
        return if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            chooser.selectedFile.toPath()
        } else null
    }
}
