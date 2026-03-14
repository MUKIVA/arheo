package ru.arheo.core.data

import ru.arheo.core.domain.model.Monument
import ru.arheo.core.domain.model.Report

internal class InMemoryReportRepository : ReportRepository {

    private var nextId: Long = 4L

    private val reports: MutableList<Report> = mutableListOf(
        Report(
            id = 1L,
            title = "Отчёт о разведках в бассейне р. Амур",
            year = 2023,
            authors = listOf("Иванов А.А.", "Петрова Б.В."),
            workType = "Разведка",
            districts = listOf("Хабаровский район"),
            keywords = listOf("неолит", "керамика", "каменный век", "амур"),
            monuments = listOf(
                Monument(
                    id = 1L,
                    name = "Амурское-1",
                    type = "Поселение",
                    culture = "Мальцевская",
                    period = "Неолит",
                    geographicLocation = "Хабаровский район",
                    number = "1",
                ),
            ),
        ),
        Report(
            id = 2L,
            title = "Результаты раскопок городища Кондон",
            year = 2022,
            authors = listOf("Сидоров В.В."),
            workType = "Экспедиция",
            districts = listOf("Солнечный район", "Комсомольский район"),
            keywords = listOf("городище", "железный век", "раскопки"),
            monuments = listOf(
                Monument(
                    id = 2L,
                    name = "Кондон-1",
                    type = "Городище",
                    culture = "Польцевская",
                    period = "Ранний железный век",
                    geographicLocation = "Солнечный район",
                    number = "2",
                ),
            ),
        ),
        Report(
            id = 3L,
            title = "Мониторинг памятников археологии Приморского края",
            year = 2021,
            authors = listOf("Козлова Г.Д.", "Николаев Е.Ж."),
            workType = "Мониторинг",
            districts = listOf("Уссурийский район", "Находкинский район"),
            keywords = listOf("мониторинг", "приморье", "охрана памятников"),
            monuments = emptyList(),
        ),
    )

    override suspend fun getAllReports(): List<Report> {
        return reports.sortedByDescending { it.year }
    }

    override suspend fun getReportById(id: Long): Report? {
        return reports.find { it.id == id }
    }

    override suspend fun searchReports(query: String): List<Report> {
        if (query.isBlank()) return getAllReports()
        val lowerQuery = query.lowercase()
        return reports.filter { report ->
            report.title.lowercase().contains(lowerQuery) ||
                report.authors.any { it.lowercase().contains(lowerQuery) } ||
                report.year.toString().contains(lowerQuery) ||
                report.workType.lowercase().contains(lowerQuery) ||
                report.districts.any { it.lowercase().contains(lowerQuery) } ||
                report.keywords.any { it.lowercase().contains(lowerQuery) } ||
                report.monuments.any { it.name.lowercase().contains(lowerQuery) }
        }.sortedByDescending { it.year }
    }

    override suspend fun addReport(report: Report): Report {
        val newReport = report.copy(id = nextId++)
        reports.add(newReport)
        return newReport
    }

    override suspend fun updateReport(report: Report) {
        val index = reports.indexOfFirst { it.id == report.id }
        if (index >= 0) {
            reports[index] = report
        }
    }

    override suspend fun deleteReport(id: Long) {
        reports.removeAll { it.id == id }
    }

    override suspend fun getAllAuthors(): List<String> {
        return reports.flatMap { it.authors }.distinct().sorted()
    }

    override suspend fun getAllWorkTypes(): List<String> {
        return reports.map { it.workType }.filter { it.isNotBlank() }.distinct().sorted()
    }

    override suspend fun getAllDistricts(): List<String> {
        return reports.flatMap { it.districts }.distinct().sorted()
    }

    override suspend fun getAllKeywords(): List<String> {
        return reports.flatMap { it.keywords }.distinct().sorted()
    }
}
