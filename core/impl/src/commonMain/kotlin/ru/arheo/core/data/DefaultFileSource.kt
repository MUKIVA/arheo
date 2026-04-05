package ru.arheo.core.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.arheo.core.domain.model.FileInfoData
import ru.arheo.core.domain.model.ReportData
import ru.arheo.core.data.util.AppPaths
import ru.arheo.core.data.util.TarGzArchive
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.security.MessageDigest
import java.util.Comparator
import java.util.UUID

internal class DefaultFileSource : FileSource {

    override suspend fun createWorkingDirectory(): String = withContext(Dispatchers.IO) {
        val dir = AppPaths.resolveTmpDirectory().resolve(UUID.randomUUID().toString())
        Files.createDirectories(dir)
        dir.toString()
    }

    override suspend fun copyToWorking(
        workingDir: String,
        sourcePaths: List<String>,
    ): Unit = withContext(Dispatchers.IO) {
        val targetDir = Path.of(workingDir)
        sourcePaths.forEach { sourcePath ->
            val source = Path.of(sourcePath)
            if (Files.isDirectory(source)) {
                copyDirectoryRecursive(source, targetDir.resolve(source.fileName))
            } else {
                Files.copy(source, targetDir.resolve(source.fileName), StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }

    override suspend fun listWorkingFiles(workingDir: String): List<FileInfoData> = withContext(Dispatchers.IO) {
        val dir = Path.of(workingDir)
        if (!Files.exists(dir)) return@withContext emptyList()
        Files.list(dir).use { stream ->
            stream
                .map { path -> buildFileInfo(path) }
                .toList()
                .sortedBy { it.name }
        }
    }

    override suspend fun removeFromWorking(
        workingDir: String,
        fileName: String,
    ): Unit = withContext(Dispatchers.IO) {
        val path = Path.of(workingDir, fileName)
        deletePathRecursive(path)
    }

    override suspend fun archiveWorkingDirectory(
        workingDir: String,
        archiveName: String,
    ): String = withContext(Dispatchers.IO) {
        val archivePath = AppPaths.resolveArchivesDirectory().resolve("$archiveName.tar.gz")
        TarGzArchive.createTarGzFromDirectoryContents(Path.of(workingDir), archivePath)
        archivePath.toString()
    }

    override suspend fun extractArchive(
        archivePath: String,
        workingDir: String,
    ): Unit = withContext(Dispatchers.IO) {
        TarGzArchive.extractTarGzToDirectory(Path.of(archivePath), Path.of(workingDir))
    }

    override suspend fun cleanupWorkingDirectory(workingDir: String): Unit = withContext(Dispatchers.IO) {
        deletePathRecursive(Path.of(workingDir))
    }

    override suspend fun deleteArchive(archivePath: String): Unit = withContext(Dispatchers.IO) {
        Files.deleteIfExists(Path.of(archivePath))
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

}
