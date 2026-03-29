package ru.arheo.feature.report.list.data

import ru.arheo.core.data.ReportSource
import ru.arheo.feature.report.list.data.mappers.DataReportMapper
import ru.arheo.feature.report.list.data.mappers.Mapper
import ru.arheo.feature.report.list.domain.ReportRepository
import ru.arheo.feature.report.list.domain.models.Report

internal class DefaultReportRepository(
    private val source: ReportSource
) : ReportRepository {
    override suspend fun getAllReports(): List<Report> {
        return source.getAllReports()
            .map(Mapper::from)
            .map(DataReportMapper::toReport)
    }

    override suspend fun getReportById(id: Long): Report? {
        return source.getReportById(id)
            ?.let(Mapper::from)
            ?.let(DataReportMapper::toReport)
    }

    override suspend fun searchReports(query: String): List<Report> {
        return source.searchReports(query)
            .map(Mapper::from)
            .map(DataReportMapper::toReport)
    }

    override suspend fun deleteReport(id: Long) {
        source.deleteReport(id)
    }
}

