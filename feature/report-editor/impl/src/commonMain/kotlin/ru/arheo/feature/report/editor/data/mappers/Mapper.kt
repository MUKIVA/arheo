package ru.arheo.feature.report.editor.data.mappers

import ru.arheo.core.domain.model.ReportData

internal object Mapper {
    fun from(data: ReportData): DataReportMapper = DataReportMapper(data)
}
