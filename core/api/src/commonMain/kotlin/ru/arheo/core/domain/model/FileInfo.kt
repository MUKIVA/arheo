package ru.arheo.core.domain.model

data class FileInfo(
    val name: String,
    val absolutePath: String,
    val size: Long,
    val isDirectory: Boolean = false,
)
