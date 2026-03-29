package ru.arheo.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportData(
    val id: Long = 0L,
    val title: String = "",
    val year: Int = 0,
    val authors: List<String> = emptyList(),
    val workType: String = "",
    val districts: List<String> = emptyList(),
    val keywords: List<String> = emptyList(),
    val monuments: List<Monument> = emptyList(),
    val reportFilePath: String? = null,
    val archiveFilePath: String? = null,
)
