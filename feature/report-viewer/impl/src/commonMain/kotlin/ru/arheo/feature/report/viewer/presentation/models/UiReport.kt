package ru.arheo.feature.report.viewer.presentation.models

import androidx.compose.runtime.Immutable
import java.nio.file.Path

@Immutable
internal data class UiReport(
    val id: Long,
    val name: String,
    val year: Int,
    val authors: String,
    val workType: String,
    val districts: String,
    val keywords: String,
    val archive: Path?,
)