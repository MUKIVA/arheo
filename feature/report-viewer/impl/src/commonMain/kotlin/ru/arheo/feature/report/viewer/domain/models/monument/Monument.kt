package ru.arheo.feature.report.viewer.domain.models.monument

internal data class Monument(
    val id: MonumentId,
    val name: MonumentName,
    val type: MonumentType,
    val culture: MonumentCulture,
    val period: MonumentPeriod,
    val location: MonumentLocation,
    val number: MonumentNumber,
)
