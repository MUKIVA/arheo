package ru.arheo.feature.report.list.domain.models

import androidx.compose.runtime.Immutable

@Immutable
internal data class Report(
    val id: Long,
    val name: String,
    val year: Int,
    val authors: List<String>,
    val workType: String
)