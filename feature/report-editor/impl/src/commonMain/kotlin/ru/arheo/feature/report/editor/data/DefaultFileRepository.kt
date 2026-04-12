package ru.arheo.feature.report.editor.data

import ru.arheo.core.data.FileSource
import ru.arheo.feature.report.editor.data.mappers.Mapper
import ru.arheo.feature.report.editor.domian.FileRepository
import ru.arheo.feature.report.editor.domian.models.report.Report
import java.nio.file.Path

internal class DefaultFileRepository(
    private val source: FileSource
) : FileRepository {
    override suspend fun createWorkingDirectory(): Path {
        return source.createWorkingDirectory()
    }
    override suspend fun archiveWorkingDirectory(
        working: Path,
        archiveName: String
    ) = source.archiveWorkingDirectory(working, archiveName)
    override suspend fun cleanupWorkingDirectory(
        working: Path
    ) = source.cleanupWorkingDirectory(working)
    override suspend fun deleteArchive(
        archive: Path
    ) = source.deleteArchive(archive)
    override fun computeArchiveName(
        report: Report
    ): String = source.computeArchiveName(Mapper.from(report).toReportData())
}
