package ru.arheo.feature.report.selector.data

import ru.arheo.core.data.FileSource
import ru.arheo.feature.report.selector.data.mappers.FileInfoDataMapper
import ru.arheo.feature.report.selector.data.mappers.Mapper
import ru.arheo.feature.report.selector.domain.FileRepository
import ru.arheo.feature.report.selector.domain.models.FileInfo
import java.nio.file.Path

internal class DefaultFileRepository(
    private val source: FileSource
) : FileRepository {
    override suspend fun copyToWorking(
        working: Path,
        sources: List<Path>
    ) = source.copyToWorking(working, sources)


    override suspend fun listWorkingFiles(
        working: Path
    ): List<FileInfo> {
        return source.listWorkingFiles(working)
            .map(Mapper::from)
            .map(FileInfoDataMapper::toDomain)
    }

    override suspend fun removeFromWorking(
        working: Path,
        fileName: String
    ) {
        source.removeFromWorking(
            working = working,
            fileName = fileName
        )
    }

    override suspend fun archiveWorkingDirectory(
        working: Path,
        archiveName: String
    ): Path = source.archiveWorkingDirectory(working, archiveName)


    override suspend fun extractArchive(
        archive: Path,
        working: Path
    ) = source.extractArchive(archive, working)


    override suspend fun cleanupWorkingDirectory(
        working: Path
    ) = source.cleanupWorkingDirectory(working)

    override suspend fun deleteArchive(
        archive: Path
    ) = source.deleteArchive(archive)
}