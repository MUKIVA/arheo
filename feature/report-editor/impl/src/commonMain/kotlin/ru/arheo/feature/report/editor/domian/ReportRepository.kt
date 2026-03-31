package ru.arheo.feature.report.editor.domian

import ru.arheo.feature.report.editor.domian.models.Author
import ru.arheo.feature.report.editor.domian.models.report.Report
import ru.arheo.feature.report.editor.domian.models.report.ReportId
import ru.arheo.feature.report.editor.domian.models.report.ReportWorkType

internal interface ReportRepository {
    suspend fun getReportById(id: ReportId): Report?
    suspend fun addReport(report: Report): Report
    suspend fun updateReport(report: Report)
    suspend fun getAllAuthors(): List<Author>
    suspend fun getAllWorkTypes(): List<ReportWorkType>
}