package ru.arheo.feature.report.editor.data

import ru.arheo.core.data.FileSource
import ru.arheo.core.domain.model.ReportData
import ru.arheo.feature.report.editor.domian.FileRepository

internal class DefaultFileRepository(
    private val source: FileSource
) : FileRepository {
    override suspend fun archiveWorkingDirectory(
        workingDir: String,
        archiveName: String
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun cleanupWorkingDirectory(workingDir: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteArchive(archivePath: String) {
        TODO("Not yet implemented")
    }

    override fun computeArchiveName(report: ReportData): String {
        TODO("Not yet implemented")
    }
}