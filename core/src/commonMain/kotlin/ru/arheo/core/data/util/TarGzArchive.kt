package ru.arheo.core.data.util

import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

internal object TarGzArchive {

    fun createTarGzFromDirectoryContents(sourceDir: Path, archiveFile: Path) {
        BufferedOutputStream(Files.newOutputStream(archiveFile)).use { bufferedOut ->
            GzipCompressorOutputStream(bufferedOut).use { gzipOut ->
                TarArchiveOutputStream(gzipOut).use { tarOut ->
                    tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX)
                    Files.walk(sourceDir).use { stream ->
                        stream
                            .filter { path -> path != sourceDir }
                            .sorted()
                            .forEach { path ->
                                val relative = sourceDir.relativize(path).toString().replace('\\', '/')
                                val entryName = if (Files.isDirectory(path)) "$relative/" else relative
                                val entry = TarArchiveEntry(path.toFile(), entryName)
                                tarOut.putArchiveEntry(entry)
                                if (Files.isRegularFile(path)) {
                                    Files.copy(path, tarOut)
                                }
                                tarOut.closeArchiveEntry()
                            }
                    }
                }
            }
        }
    }

    fun extractTarGzToDirectory(archiveFile: Path, destDir: Path) {
        Files.createDirectories(destDir)
        BufferedInputStream(Files.newInputStream(archiveFile)).use { bufferedIn ->
            GzipCompressorInputStream(bufferedIn).use { gzipIn ->
                TarArchiveInputStream(gzipIn).use { tarIn ->
                    while (true) {
                        val entry: ArchiveEntry = tarIn.nextEntry ?: break
                        extractOneEntry(destDir, tarIn, entry)
                    }
                }
            }
        }
    }

    private fun extractOneEntry(destDir: Path, tarIn: TarArchiveInputStream, entry: ArchiveEntry) {
        val rawName = entry.name.trimStart('/').replace('\\', '/')
        if (rawName.isEmpty()) return
        val outPath = destDir.resolve(rawName).normalize()
        if (!outPath.startsWith(destDir.normalize())) {
            throw IllegalArgumentException("Unsafe path in archive: ${entry.name}")
        }
        if (entry.isDirectory) {
            Files.createDirectories(outPath)
        } else {
            Files.createDirectories(outPath.parent)
            Files.copy(tarIn, outPath, StandardCopyOption.REPLACE_EXISTING)
        }
    }
}
