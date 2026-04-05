package ru.arheo.feature.report.viewer.domain

import ru.arheo.feature.report.viewer.domain.models.Report
import ru.arheo.feature.report.viewer.domain.models.ReportId

internal interface ReportRepository {
    suspend fun getReportById(id: ReportId): Report?
}