package ru.arheo.feature.report.selector.domain.models

internal data class FileInfo(
    val name: FileInfoName,
    val absolutePath: FileInfoAbsolutePath,
    val size: FileInfoSize,
    val isDirectory: FileInfoIsDirectory = FileInfoIsDirectory(),
)