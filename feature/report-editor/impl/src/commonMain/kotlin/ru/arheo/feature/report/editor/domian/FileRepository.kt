package ru.arheo.feature.report.editor.domian

import ru.arheo.core.domain.model.ReportData

internal interface FileRepository {
    suspend fun archiveWorkingDirectory(workingDir: String, archiveName: String): String
    suspend fun cleanupWorkingDirectory(workingDir: String)
    suspend fun deleteArchive(archivePath: String)
    fun computeArchiveName(report: ReportData): String
}