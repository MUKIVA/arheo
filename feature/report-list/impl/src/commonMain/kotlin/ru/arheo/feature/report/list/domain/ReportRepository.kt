package ru.arheo.feature.report.list.domain

import ru.arheo.feature.report.list.domain.models.Report

internal interface ReportRepository {
    suspend fun getAllReports(): List<Report>
    suspend fun getReportById(id: Long): Report?
    suspend fun searchReports(query: String): List<Report>
    suspend fun deleteReport(id: Long)
}