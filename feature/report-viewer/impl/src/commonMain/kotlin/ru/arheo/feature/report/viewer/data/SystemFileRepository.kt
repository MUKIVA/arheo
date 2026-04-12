package ru.arheo.feature.report.viewer.data

import ru.arheo.core.data.FileSource
import ru.arheo.feature.report.viewer.domain.FileRepository
import java.nio.file.Path
import kotlin.io.path.exists

internal class SystemFileRepository(
    private val source: FileSource
) : FileRepository {

    override suspend fun createWorkingDirectory(): Path {
        return source.createWorkingDirectory()
    }

    override suspend fun extractArchive(working: Path, archive: Path) {
        source.extractArchive(archive, working)
    }

}