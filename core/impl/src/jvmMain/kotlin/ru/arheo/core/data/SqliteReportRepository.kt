package ru.arheo.core.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.lowerCase
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import ru.arheo.core.data.db.MonumentsTable
import ru.arheo.core.data.db.ReportAuthorsTable
import ru.arheo.core.data.db.ReportDistrictsTable
import ru.arheo.core.data.db.ReportKeywordsTable
import ru.arheo.core.data.db.ReportsTable
import ru.arheo.core.domain.model.Monument
import ru.arheo.core.domain.model.Report

internal class SqliteReportRepository(
    private val database: Database,
) : ReportRepository {

    private suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) { transaction(database) { block() } }

    override suspend fun getAllReports(): List<Report> = dbQuery {
        loadAllReports()
    }

    override suspend fun getReportById(id: Long): Report? = dbQuery {
        loadReportById(id)
    }

    override suspend fun searchReports(query: String): List<Report> = dbQuery {
        if (query.isBlank()) return@dbQuery loadAllReports()
        val searchTerm = query.lowercase()
        val pattern = "%$searchTerm%"
        val matchingIds = findMatchingReportIds(pattern, searchTerm)
        matchingIds
            .mapNotNull { loadReportById(it) }
            .sortedByDescending { it.year }
    }

    override suspend fun addReport(report: Report): Report = dbQuery {
        val reportId = ReportsTable.insertAndGetId {
            it[title] = report.title
            it[year] = report.year
            it[workType] = report.workType
            it[reportFilePath] = report.reportFilePath
            it[archiveFilePath] = report.archiveFilePath
        }.value
        insertRelatedData(reportId, report)
        loadReportById(reportId)!!
    }

    override suspend fun updateReport(report: Report): Unit = dbQuery {
        ReportsTable.update({ ReportsTable.id eq report.id }) {
            it[title] = report.title
            it[year] = report.year
            it[workType] = report.workType
            it[reportFilePath] = report.reportFilePath
            it[archiveFilePath] = report.archiveFilePath
        }
        deleteRelatedData(report.id)
        insertRelatedData(report.id, report)
    }

    override suspend fun deleteReport(id: Long): Unit = dbQuery {
        deleteRelatedData(id)
        ReportsTable.deleteWhere { ReportsTable.id eq id }
    }

    override suspend fun getAllAuthors(): List<String> = dbQuery {
        ReportAuthorsTable
            .selectAll()
            .map { it[ReportAuthorsTable.author] }
            .distinct()
            .sorted()
    }

    override suspend fun getAllWorkTypes(): List<String> = dbQuery {
        ReportsTable
            .selectAll()
            .map { it[ReportsTable.workType] }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    override suspend fun getAllDistricts(): List<String> = dbQuery {
        ReportDistrictsTable
            .selectAll()
            .map { it[ReportDistrictsTable.district] }
            .distinct()
            .sorted()
    }

    override suspend fun getAllKeywords(): List<String> = dbQuery {
        ReportKeywordsTable
            .selectAll()
            .map { it[ReportKeywordsTable.keyword] }
            .distinct()
            .sorted()
    }

    private fun loadAllReports(): List<Report> =
        ReportsTable
            .selectAll()
            .orderBy(ReportsTable.year to SortOrder.DESC)
            .map { row -> buildReport(row) }

    private fun loadReportById(id: Long): Report? {
        val row = ReportsTable
            .selectAll()
            .where { ReportsTable.id eq id }
            .singleOrNull() ?: return null
        return buildReport(row)
    }

    private fun buildReport(row: ResultRow): Report {
        val reportId = row[ReportsTable.id].value
        return Report(
            id = reportId,
            title = row[ReportsTable.title],
            year = row[ReportsTable.year],
            workType = row[ReportsTable.workType],
            reportFilePath = row[ReportsTable.reportFilePath],
            archiveFilePath = row[ReportsTable.archiveFilePath],
            authors = loadAuthorsForReport(reportId),
            districts = loadDistrictsForReport(reportId),
            keywords = loadKeywordsForReport(reportId),
            monuments = loadMonumentsForReport(reportId),
        )
    }

    private fun loadAuthorsForReport(reportId: Long): List<String> =
        ReportAuthorsTable
            .selectAll()
            .where { ReportAuthorsTable.reportId eq reportId }
            .map { it[ReportAuthorsTable.author] }

    private fun loadDistrictsForReport(reportId: Long): List<String> =
        ReportDistrictsTable
            .selectAll()
            .where { ReportDistrictsTable.reportId eq reportId }
            .map { it[ReportDistrictsTable.district] }

    private fun loadKeywordsForReport(reportId: Long): List<String> =
        ReportKeywordsTable
            .selectAll()
            .where { ReportKeywordsTable.reportId eq reportId }
            .map { it[ReportKeywordsTable.keyword] }

    private fun loadMonumentsForReport(reportId: Long): List<Monument> =
        MonumentsTable
            .selectAll()
            .where { MonumentsTable.reportId eq reportId }
            .map { row ->
                Monument(
                    id = row[MonumentsTable.id].value,
                    name = row[MonumentsTable.name],
                    type = row[MonumentsTable.type],
                    culture = row[MonumentsTable.culture],
                    period = row[MonumentsTable.period],
                    geographicLocation = row[MonumentsTable.geographicLocation],
                    number = row[MonumentsTable.number],
                )
            }

    private fun findMatchingReportIds(pattern: String, searchTerm: String): Set<Long> {
        val ids = mutableSetOf<Long>()
        ReportsTable.selectAll().where {
            (ReportsTable.title.lowerCase() like pattern) or
                (ReportsTable.workType.lowerCase() like pattern)
        }.forEach { ids.add(it[ReportsTable.id].value) }
        ReportsTable.selectAll().forEach { row ->
            if (row[ReportsTable.year].toString().contains(searchTerm)) {
                ids.add(row[ReportsTable.id].value)
            }
        }
        ReportAuthorsTable.selectAll().where {
            ReportAuthorsTable.author.lowerCase() like pattern
        }.forEach { ids.add(it[ReportAuthorsTable.reportId]) }
        ReportDistrictsTable.selectAll().where {
            ReportDistrictsTable.district.lowerCase() like pattern
        }.forEach { ids.add(it[ReportDistrictsTable.reportId]) }
        ReportKeywordsTable.selectAll().where {
            ReportKeywordsTable.keyword.lowerCase() like pattern
        }.forEach { ids.add(it[ReportKeywordsTable.reportId]) }
        MonumentsTable.selectAll().where {
            MonumentsTable.name.lowerCase() like pattern
        }.forEach { ids.add(it[MonumentsTable.reportId]) }
        return ids
    }

    private fun insertRelatedData(reportId: Long, report: Report) {
        report.authors.forEach { author ->
            ReportAuthorsTable.insert {
                it[ReportAuthorsTable.reportId] = reportId
                it[ReportAuthorsTable.author] = author
            }
        }
        report.districts.forEach { district ->
            ReportDistrictsTable.insert {
                it[ReportDistrictsTable.reportId] = reportId
                it[ReportDistrictsTable.district] = district
            }
        }
        report.keywords.forEach { keyword ->
            ReportKeywordsTable.insert {
                it[ReportKeywordsTable.reportId] = reportId
                it[ReportKeywordsTable.keyword] = keyword
            }
        }
        report.monuments.forEach { monument ->
            MonumentsTable.insert {
                it[MonumentsTable.reportId] = reportId
                it[MonumentsTable.name] = monument.name
                it[MonumentsTable.type] = monument.type
                it[MonumentsTable.culture] = monument.culture
                it[MonumentsTable.period] = monument.period
                it[MonumentsTable.geographicLocation] = monument.geographicLocation
                it[MonumentsTable.number] = monument.number
            }
        }
    }

    private fun deleteRelatedData(reportId: Long) {
        ReportAuthorsTable.deleteWhere { ReportAuthorsTable.reportId eq reportId }
        ReportDistrictsTable.deleteWhere { ReportDistrictsTable.reportId eq reportId }
        ReportKeywordsTable.deleteWhere { ReportKeywordsTable.reportId eq reportId }
        MonumentsTable.deleteWhere { MonumentsTable.reportId eq reportId }
    }
}
