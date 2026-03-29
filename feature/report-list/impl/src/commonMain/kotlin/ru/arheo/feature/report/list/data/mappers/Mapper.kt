package ru.arheo.feature.report.list.data.mappers

import ru.arheo.core.domain.model.ReportData

internal object Mapper {
    fun from(data: ReportData): DataReportMapper {
        return DataReportMapper(data)
    }
}