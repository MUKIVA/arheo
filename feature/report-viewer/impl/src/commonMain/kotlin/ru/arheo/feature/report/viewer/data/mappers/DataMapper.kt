package ru.arheo.feature.report.viewer.data.mappers

import ru.arheo.core.domain.model.ReportData

internal object DataMapper {
    fun from(reportData: ReportData): ReportDataMapper {
        return ReportDataMapper(reportData)
    }
}