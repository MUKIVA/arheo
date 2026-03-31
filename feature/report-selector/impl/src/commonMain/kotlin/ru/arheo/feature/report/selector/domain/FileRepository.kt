package ru.arheo.feature.report.selector.domain

import ru.arheo.feature.report.selector.domain.models.FileInfo

internal interface FileRepository {
    suspend fun createWorkingDirectory(): String
    suspend fun copyToWorking(
        workingDir: String,
        sourcePaths: List<String>
    )
    suspend fun listWorkingFiles(
        workingDir: String
    ): List<FileInfo>
    suspend fun removeFromWorking(
        workingDir: String,
        fileName: String
    )
    suspend fun archiveWorkingDirectory(
        workingDir: String,
        archiveName: String
    ): String
    suspend fun extractArchive(
        archivePath: String,
        workingDir: String
    )
    suspend fun cleanupWorkingDirectory(
        workingDir: String
    )
    suspend fun deleteArchive(
        archivePath: String
    )
}