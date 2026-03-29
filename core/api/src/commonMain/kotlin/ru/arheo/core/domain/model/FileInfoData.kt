package ru.arheo.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FileInfoData(
    val name: String,
    val absolutePath: String,
    val size: Long,
    val isDirectory: Boolean = false,
)
