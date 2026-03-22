package ru.arheo.core.data

import ru.arheo.core.domain.model.FileInfo
import ru.arheo.core.domain.model.Report

interface FileManager {
    suspend fun createWorkingDirectory(): String
    suspend fun copyToWorking(workingDir: String, sourcePaths: List<String>)
    suspend fun listWorkingFiles(workingDir: String): List<FileInfo>
    suspend fun removeFromWorking(workingDir: String, fileName: String)
    suspend fun archiveWorkingDirectory(workingDir: String, archiveName: String): String
    suspend fun extractArchive(archivePath: String, workingDir: String)
    suspend fun cleanupWorkingDirectory(workingDir: String)
    suspend fun deleteArchive(archivePath: String)
    fun computeArchiveName(report: Report): String
}
