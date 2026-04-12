package ru.arheo.feature.report.editor.data.mappers

import ru.arheo.core.domain.model.ReportData
import ru.arheo.feature.report.editor.domian.models.report.Report

internal object Mapper {
    fun from(data: ReportData): DataReportMapper = DataReportMapper(data)
    fun from(report: Report) = DomainReportMapper(report)
}
