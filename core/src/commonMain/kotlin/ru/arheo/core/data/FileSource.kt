package ru.arheo.core.data

import ru.arheo.core.domain.model.FileInfoData
import ru.arheo.core.domain.model.ReportData
import java.nio.file.Path

interface FileSource {
    suspend fun createWorkingDirectory(): Path
    suspend fun copyToWorking(working: Path, sources: List<Path>)
    suspend fun listWorkingFiles(working: Path): List<FileInfoData>
    suspend fun removeFromWorking(working: Path, fileName: String)
    suspend fun archiveWorkingDirectory(working: Path, archiveName: String): Path
    suspend fun extractArchive(archive: Path, working: Path)
    suspend fun cleanupWorkingDirectory(working: Path)
    suspend fun deleteArchive(archive: Path)
    fun computeArchiveName(report: ReportData): String
}
