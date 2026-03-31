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
    ): String = source.archiveWorkingDirectory(workingDir, archiveName)

    override suspend fun cleanupWorkingDirectory(
        workingDir: String
    ) = source.cleanupWorkingDirectory(workingDir)

    override suspend fun deleteArchive(
        archivePath: String
    ) = source.deleteArchive(archivePath)

    override fun computeArchiveName(
        report: ReportData
    ): String = source.computeArchiveName(report)
}
