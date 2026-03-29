package ru.arheo.core.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.arheo.core.domain.model.FileInfo
import ru.arheo.core.domain.model.DataReport
import ru.arheo.core.data.util.AppPaths
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.security.MessageDigest
import java.util.Comparator
import java.util.UUID

internal class DefaultFileManager : FileManager {

    private val isWindows: Boolean =
        System.getProperty("os.name").lowercase().contains("win")

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

    override suspend fun listWorkingFiles(workingDir: String): List<FileInfo> = withContext(Dispatchers.IO) {
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
        val script = resolveScript(ARCHIVE_SCRIPT_SH, ARCHIVE_SCRIPT_BAT)
        executeScript(script, workingDir, archivePath.toString())
        archivePath.toString()
    }

    override suspend fun extractArchive(
        archivePath: String,
        workingDir: String,
    ): Unit = withContext(Dispatchers.IO) {
        val script = resolveScript(EXTRACT_SCRIPT_SH, EXTRACT_SCRIPT_BAT)
        executeScript(script, archivePath, workingDir)
    }

    override suspend fun cleanupWorkingDirectory(workingDir: String): Unit = withContext(Dispatchers.IO) {
        deletePathRecursive(Path.of(workingDir))
    }

    override suspend fun deleteArchive(archivePath: String): Unit = withContext(Dispatchers.IO) {
        Files.deleteIfExists(Path.of(archivePath))
    }

    override fun computeArchiveName(report: DataReport): String {
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

    private fun resolveScript(shName: String, batName: String): Path {
        val scriptsDir = AppPaths.resolveScriptsDirectory()
        return if (isWindows) scriptsDir.resolve(batName) else scriptsDir.resolve(shName)
    }

    private fun executeScript(scriptPath: Path, vararg args: String) {
        val command = if (isWindows) {
            listOf("cmd", "/c", scriptPath.toString()) + args
        } else {
            listOf("bash", scriptPath.toString()) + args
        }
        val process = ProcessBuilder(command)
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readText()
        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("Script ${scriptPath.fileName} failed (exit $exitCode): $output")
        }
    }

    private fun buildFileInfo(path: Path): FileInfo =
        FileInfo(
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

    companion object {
        private const val ARCHIVE_SCRIPT_SH = "archive.sh"
        private const val ARCHIVE_SCRIPT_BAT = "archive.bat"
        private const val EXTRACT_SCRIPT_SH = "extract.sh"
        private const val EXTRACT_SCRIPT_BAT = "extract.bat"
    }
}
