package ru.arheo.feature.report.editor.data

import ru.arheo.core.data.ReportSource
import ru.arheo.feature.report.editor.domian.ReportRepository
import ru.arheo.feature.report.editor.domian.models.Author
import ru.arheo.feature.report.editor.domian.models.report.Report
import ru.arheo.feature.report.editor.domian.models.report.ReportId
import ru.arheo.feature.report.editor.domian.models.report.ReportWorkType

internal class DefaultReportRepository(
    private val source: ReportSource
) : ReportRepository {
    override suspend fun getReportById(id: ReportId): Report? {
        TODO("Not yet implemented")
    }
    override suspend fun addReport(report: Report): Report {
        TODO("Not yet implemented")
    }
    override suspend fun updateReport(report: Report) {
        TODO("Not yet implemented")
    }
    override suspend fun getAllAuthors(): List<Author> {
        return source.getAllAuthors()
            .map { data -> Author(data) }
    }

    override suspend fun getAllWorkTypes(): List<ReportWorkType> {
        return source.getAllWorkTypes()
            .map { data -> ReportWorkType(data) }
    }
}