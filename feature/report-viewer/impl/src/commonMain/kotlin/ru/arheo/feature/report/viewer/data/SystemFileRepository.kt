package ru.arheo.feature.report.viewer.data

import ru.arheo.core.data.FileSource
import ru.arheo.feature.report.viewer.domain.FileRepository
import java.awt.Desktop
import java.io.File

internal class SystemFileRepository(
    private val fileSource: FileSource
) : FileRepository {

    override suspend fun extractArchiveAndOpenInExplorer(archiveFilePath: String?): Boolean {
        if (archiveFilePath.isNullOrBlank()) {
            return false
        }
        val archiveFile = File(archiveFilePath)
        if (!archiveFile.exists()) {
            return false
        }
        if (archiveFile.isDirectory) {
            return openDirectoryInSystemExplorer(archiveFilePath)
        }
        if (!archiveFile.isFile) {
            return false
        }
        val workingDir = fileSource.createWorkingDirectory()
        return try {
            fileSource.extractArchive(archiveFilePath, workingDir)
            if (openDirectoryInSystemExplorer(workingDir)) {
                true
            } else {
                fileSource.cleanupWorkingDirectory(workingDir)
                false
            }
        } catch (_: Exception) {
            try {
                fileSource.cleanupWorkingDirectory(workingDir)
            } catch (_: Exception) {
            }
            false
        }
    }

    private fun openDirectoryInSystemExplorer(absolutePath: String): Boolean {
        val directory = File(absolutePath)
        if (!directory.exists() || !directory.isDirectory) {
            return false
        }
        return try {
            if (!Desktop.isDesktopSupported()) {
                return false
            }
            Desktop.getDesktop().open(directory)
            true
        } catch (_: Exception) {
            false
        }
    }

}