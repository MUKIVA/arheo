package ru.arheo.feature.report.editor.domian.models.monument

internal data class Monument(
    val id: MonumentId,
    val name: MonumentName,
    val type: MonumentType,
    val culture: MonumentCulture,
    val period: MonumentPeriod,
    val geographicLocation: MonumentLocation,
    val number: MonumentNumber
)
