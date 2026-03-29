package ru.arheo.feature.report.list.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
internal data class UiReport(
    val id: Long,
    val name: String,
    val authors: List<String>,
    val year: Int,
    val workType: String,
)