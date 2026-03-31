package ru.arheo.feature.report.selector.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
internal data class UiFileInfo(
    val name: String,
    val size: Long,
    val isDirectory: Boolean,
)
