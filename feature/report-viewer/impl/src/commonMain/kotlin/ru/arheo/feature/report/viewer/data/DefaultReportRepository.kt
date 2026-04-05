package ru.arheo.feature.report.viewer.data

import ru.arheo.core.data.ReportSource
import ru.arheo.feature.report.viewer.data.mappers.DataMapper
import ru.arheo.feature.report.viewer.data.mappers.ReportDataMapper
import ru.arheo.feature.report.viewer.domain.ReportRepository
import ru.arheo.feature.report.viewer.domain.models.report.Report
import ru.arheo.feature.report.viewer.domain.models.report.ReportId

internal class DefaultReportRepository(
    private val source: ReportSource
) : ReportRepository {
    override suspend fun getReportById(id: ReportId): Report? {
        return source.getReportById(id.value)
            ?.let(DataMapper::from)
            ?.let(ReportDataMapper::toDomain)
    }

}