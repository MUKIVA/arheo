package ru.arheo.feature.report.selector.data

import ru.arheo.core.data.FileSource
import ru.arheo.feature.report.selector.data.mappers.FileInfoDataMapper
import ru.arheo.feature.report.selector.data.mappers.Mapper
import ru.arheo.feature.report.selector.domain.FileRepository
import ru.arheo.feature.report.selector.domain.models.FileInfo

internal class DefaultFileRepository(
    private val source: FileSource
) : FileRepository {
    override suspend fun createWorkingDirectory(): String {
        return source.createWorkingDirectory()
    }

    override suspend fun copyToWorking(
        workingDir: String,
        sourcePaths: List<String>
    ) = source.copyToWorking(
        workingDir = workingDir,
        sourcePaths = sourcePaths
    )


    override suspend fun listWorkingFiles(
        workingDir: String
    ): List<FileInfo> {
        return source.listWorkingFiles(workingDir)
            .map(Mapper::from)
            .map(FileInfoDataMapper::toDomain)
    }

    override suspend fun removeFromWorking(
        workingDir: String,
        fileName: String
    ) {
        source.removeFromWorking(
            workingDir = workingDir,
            fileName = fileName
        )
    }

    override suspend fun archiveWorkingDirectory(
        workingDir: String,
        archiveName: String
    ): String {
        return source.archiveWorkingDirectory(
            workingDir = workingDir,
            archiveName = archiveName
        )
    }

    override suspend fun extractArchive(
        archivePath: String,
        workingDir: String
    ) = source.extractArchive(
        archivePath = archivePath,
        workingDir = workingDir
    )


    override suspend fun cleanupWorkingDirectory(
        workingDir: String
    ) = source.cleanupWorkingDirectory(
        workingDir = workingDir
    )

    override suspend fun deleteArchive(
        archivePath: String
    ) = source.deleteArchive(
        archivePath = archivePath
    )
}