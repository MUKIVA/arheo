package ru.arheo.feature.report.editor.data

import ru.arheo.core.data.ReportSource
import ru.arheo.feature.report.editor.data.mappers.DataReportMapper
import ru.arheo.feature.report.editor.data.mappers.DomainReportMapper
import ru.arheo.feature.report.editor.data.mappers.Mapper
import ru.arheo.feature.report.editor.domian.ReportRepository
import ru.arheo.feature.report.editor.domian.models.Author
import ru.arheo.feature.report.editor.domian.models.report.Report
import ru.arheo.feature.report.editor.domian.models.report.ReportId
import ru.arheo.feature.report.editor.domian.models.report.ReportWorkType

internal class DefaultReportRepository(
    private val source: ReportSource
) : ReportRepository {

    override suspend fun getReportById(id: ReportId): Report? =
        source.getReportById(id.value)
            ?.let(Mapper::from)
            ?.let(DataReportMapper::toReport)

    override suspend fun addReport(report: Report): Report {
        val data = DomainReportMapper(report).toReportData()
        val saved = source.addReport(data)
        return Mapper.from(saved).toReport()
    }

    override suspend fun updateReport(report: Report) {
        val data = DomainReportMapper(report).toReportData()
        source.updateReport(data)
    }

    override suspend fun getAllAuthors(): List<Author> =
        source.getAllAuthors().map { Author(it) }

    override suspend fun getAllWorkTypes(): List<ReportWorkType> =
        source.getAllWorkTypes().map { ReportWorkType(it) }
}
