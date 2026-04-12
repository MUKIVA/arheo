package ru.arheo.core.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.arheo.core.domain.model.FileInfoData
import ru.arheo.core.domain.model.ReportData
import ru.arheo.core.data.util.TarGzArchive
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.MessageDigest
import java.util.Comparator
import java.util.UUID

internal class DefaultFileSource : FileSource {

    override suspend fun createWorkingDirectory(): Path = withContext(Dispatchers.IO) {
        val workingDir = Paths.get(WORKING_DIRECTORY_SCOPE_NAME)
            .resolve(UUID.randomUUID().toString())
        Files.createDirectories(workingDir)
    }

    override suspend fun copyToWorking(
        working: Path,
        sources: List<Path>,
    ): Unit = withContext(Dispatchers.IO) {
        sources.forEach { source ->
            when {
                Files.isDirectory(source) ->
                    copyDirectoryRecursive(source, working.resolve(source.fileName))
                else -> Files.copy(
                    /* source = */     source,
                    /* target = */     working.resolve(source.fileName),
                    /* ...options = */ StandardCopyOption.REPLACE_EXISTING
                )
            }
        }
    }

    override suspend fun listWorkingFiles(working: Path): List<FileInfoData> = withContext(Dispatchers.IO) {
        if (!Files.exists(working)) return@withContext emptyList()
        Files.list(working).use { stream ->
            stream
                .map { path -> buildFileInfo(path) }
                .toList()
                .sortedBy { it.name }
        }
    }

    override suspend fun removeFromWorking(
        working: Path,
        fileName: String,
    ): Unit = withContext(Dispatchers.IO) {
        val path = working.resolve(fileName)
        deletePathRecursive(path)
    }

    override suspend fun archiveWorkingDirectory(
        working: Path,
        archiveName: String,
    ): Path = withContext(Dispatchers.IO) {
        val archiveScope = Path.of(ARCHIVES_DIRECTORY_SCOPE_NAME)
        Files.createDirectories(archiveScope)
        val archive = archiveScope.resolve("$archiveName.tar.gz")
        TarGzArchive.createTarGzFromDirectoryContents(working, archive)
        return@withContext archive
    }

    override suspend fun extractArchive(
        archive: Path,
        working: Path
    ) = withContext(Dispatchers.IO) {
        TarGzArchive.extractTarGzToDirectory(archive, working)
    }

    override suspend fun cleanupWorkingDirectory(
        working: Path
    ) = withContext(Dispatchers.IO) {
        deletePathRecursive(working)
    }

    override suspend fun deleteArchive(
        archive: Path
    ): Unit = withContext(Dispatchers.IO) {
        Files.deleteIfExists(archive)
    }

    override fun computeArchiveName(report: ReportData): String {
        val content = buildString {
            append(report.title)
            append("|").append(report.year)
            append("|").append(report.authors.sorted().joinToString(","))
            append("|").append(report.workType)
            append("|").append(report.districts.sorted().joinToString(","))
            append("|").append(report.keywords.sorted().joinToString(","))
        }
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(content.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }.take(32)
    }

    private fun buildFileInfo(path: Path): FileInfoData =
        FileInfoData(
            name = path.fileName.toString(),
            absolutePath = path.toString(),
            size = if (Files.isDirectory(path)) computeDirectorySize(path) else Files.size(path),
            isDirectory = Files.isDirectory(path),
        )

    private fun computeDirectorySize(dir: Path): Long =
        Files.walk(dir).filter { Files.isRegularFile(it) }.mapToLong { Files.size(it) }.sum()

    private fun copyDirectoryRecursive(source: Path, target: Path) {
        Files.walk(source).forEach { path ->
            val targetPath = target.resolve(source.relativize(path))
            if (Files.isDirectory(path)) {
                Files.createDirectories(targetPath)
            } else {
                Files.createDirectories(targetPath.parent)
                Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }

    private fun deletePathRecursive(path: Path) {
        if (!Files.exists(path)) return
        if (Files.isDirectory(path)) {
            Files.walk(path).sorted(Comparator.reverseOrder()).forEach(Files::delete)
        } else {
            Files.deleteIfExists(path)
        }
    }

    private companion object {
        const val WORKING_DIRECTORY_SCOPE_NAME = "tmp"
        const val ARCHIVES_DIRECTORY_SCOPE_NAME = "data/archives"
    }
}
