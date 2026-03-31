package ru.arheo.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MonumentData(
    val id: Long = 0L,
    val name: String = "",
    val type: String = "",
    val culture: String = "",
    val period: String = "",
    val geographicLocation: String = "",
    val number: String = "",
)
