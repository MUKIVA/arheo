package ru.arheo.core.data

import ru.arheo.core.domain.model.DataReport

interface ReportSource {
    suspend fun getAllReports(): List<DataReport>
    suspend fun getReportById(id: Long): DataReport?
    suspend fun searchReports(query: String): List<DataReport>
    suspend fun addReport(report: DataReport): DataReport
    suspend fun updateReport(report: DataReport)
    suspend fun deleteReport(id: Long)
    suspend fun getAllAuthors(): List<String>
    suspend fun getAllWorkTypes(): List<String>
    suspend fun getAllDistricts(): List<String>
    suspend fun getAllKeywords(): List<String>
}