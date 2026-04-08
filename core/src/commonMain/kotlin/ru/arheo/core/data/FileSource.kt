package ru.arheo.core.data

import ru.arheo.core.domain.model.FileInfoData
import ru.arheo.core.domain.model.ReportData

interface FileSource {
    suspend fun createWorkingDirectory(): String
    suspend fun copyToWorking(workingDir: String, sourcePaths: List<String>)
    suspend fun listWorkingFiles(workingDir: String): List<FileInfoData>
    suspend fun removeFromWorking(workingDir: String, fileName: String)
    suspend fun archiveWorkingDirectory(workingDir: String, archiveName: String): String
    suspend fun extractArchive(archivePath: String, workingDir: String)
    suspend fun cleanupWorkingDirectory(workingDir: String)
    suspend fun deleteArchive(archivePath: String)
    fun computeArchiveName(report: ReportData): String
}
