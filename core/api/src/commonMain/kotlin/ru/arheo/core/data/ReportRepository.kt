package ru.arheo.core.data

import ru.arheo.core.domain.model.Report

interface ReportRepository {
    suspend fun getAllReports(): List<Report>
    suspend fun getReportById(id: Long): Report?
    suspend fun searchReports(query: String): List<Report>
    suspend fun addReport(report: Report): Report
    suspend fun updateReport(report: Report)
    suspend fun deleteReport(id: Long)
    suspend fun getAllAuthors(): List<String>
    suspend fun getAllWorkTypes(): List<String>
    suspend fun getAllDistricts(): List<String>
    suspend fun getAllKeywords(): List<String>
}
