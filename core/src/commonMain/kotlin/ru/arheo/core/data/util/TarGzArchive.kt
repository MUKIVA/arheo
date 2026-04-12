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

    fun createTarGzFromDirectoryContents(source: Path, archive: Path) {
        val stream = Files.newOutputStream(archive)
            .let(::BufferedOutputStream)
            .let(::GzipCompressorOutputStream)
            .let(::TarArchiveOutputStream)
        stream.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX)

        Files.walk(source).apply {
            this.filter { path -> path != source }
                .sorted()
                .forEach { path -> archiveEntry(source, path, stream) }
        }
        stream.close()
    }

    fun extractTarGzToDirectory(archive: Path, dest: Path) {
        val stream = Files.newInputStream(archive)
            .let(::BufferedInputStream)
            .let(::GzipCompressorInputStream)
            .let(::TarArchiveInputStream)

        while (true) { extractOneEntry(dest,stream.nextEntry ?: break, stream) }
        stream.close()
    }

    private fun extractOneEntry(dest: Path, entry: ArchiveEntry, stream: TarArchiveInputStream) {
        val out = entry.name
            .trimStart('/')
            .replace('\\', '/')
            .takeIf { it.isNotEmpty()  }
            ?.let { dest.resolve(it).normalize() }
            ?.takeIf { it.startsWith(dest.normalize()) }
            ?: return

        if (entry.isDirectory) {
            Files.createDirectories(out)
        } else {
            Files.createDirectories(out.parent)
            Files.copy(stream, out, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    private fun archiveEntry(source: Path, path: Path, stream: TarArchiveOutputStream) {
        val entry = source.relativize(path)
            .toString()
            .replace('\\', '/')
            .let { if (Files.isDirectory(path)) "$it/" else it }
            .let { TarArchiveEntry(path.toFile(), it) }
        stream.putArchiveEntry(entry)
        if (Files.isRegularFile(path)) {
            Files.copy(path, stream)
        }
        stream.closeArchiveEntry()
    }
}
