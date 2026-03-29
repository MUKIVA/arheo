package ru.arheo.core.data

import ru.arheo.core.domain.model.ReportData

interface ReportSource {
    suspend fun getAllReports(): List<ReportData>
    suspend fun getReportById(id: Long): ReportData?
    suspend fun searchReports(query: String): List<ReportData>
    suspend fun addReport(report: ReportData): ReportData
    suspend fun updateReport(report: ReportData)
    suspend fun deleteReport(id: Long)
    suspend fun getAllAuthors(): List<String>
    suspend fun getAllWorkTypes(): List<String>
    suspend fun getAllDistricts(): List<String>
    suspend fun getAllKeywords(): List<String>
}