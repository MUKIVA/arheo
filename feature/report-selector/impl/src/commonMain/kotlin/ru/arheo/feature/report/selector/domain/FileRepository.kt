package ru.arheo.feature.report.selector.domain

import ru.arheo.feature.report.selector.domain.models.FileInfo
import java.nio.file.Path

internal interface FileRepository {
    suspend fun copyToWorking(working: Path, sources: List<Path>)
    suspend fun listWorkingFiles(working: Path): List<FileInfo>
    suspend fun removeFromWorking(working: Path, fileName: String)
    suspend fun archiveWorkingDirectory(working: Path, archiveName: String): Path
    suspend fun extractArchive(archive: Path, working: Path)
    suspend fun cleanupWorkingDirectory(working: Path)
    suspend fun deleteArchive(archive: Path)
}