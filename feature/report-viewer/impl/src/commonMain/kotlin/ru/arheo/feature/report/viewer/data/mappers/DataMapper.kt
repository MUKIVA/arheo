package ru.arheo.feature.report.viewer.data.mappers

import ru.arheo.core.domain.model.MonumentData
import ru.arheo.core.domain.model.ReportData

internal object DataMapper {
    fun from(reportData: ReportData): ReportDataMapper {
        return ReportDataMapper(reportData)
    }

    fun from(monumentData: MonumentData): MonumentDataMapper {
        return MonumentDataMapper(monumentData)
    }
}