package ru.arheo.feature.report.viewer.presentation.mappers

import ru.arheo.feature.report.viewer.domain.models.monument.Monument
import ru.arheo.feature.report.viewer.domain.models.report.Report

internal object UiMapper {
    fun from(report: Report): ReportMapper {
        return ReportMapper(report)
    }

    fun from(monument: Monument): MonumentMapper {
        return MonumentMapper(monument)
    }
}