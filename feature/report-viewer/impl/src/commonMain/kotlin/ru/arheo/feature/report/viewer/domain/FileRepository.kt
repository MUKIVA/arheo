package ru.arheo.feature.report.viewer.domain

import java.nio.file.Path

internal interface FileRepository {

    suspend fun createWorkingDirectory(): Path
    suspend fun extractArchive(working: Path, archive: Path)

}