package ru.arheo.feature.report.editor.domian

import ru.arheo.feature.report.editor.domian.models.report.Report
import java.nio.file.Path


internal interface FileRepository {
    suspend fun createWorkingDirectory(): Path
    suspend fun archiveWorkingDirectory(
        working: Path,
        archiveName: String
    ): Path
    suspend fun cleanupWorkingDirectory(
        working: Path
    )
    suspend fun deleteArchive(
        archive: Path
    )
    fun computeArchiveName(
        report: Report
    ): String
}