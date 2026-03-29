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
import ru.arheo.core.db.MonumentTable
import ru.arheo.core.db.ReportAuthorTable
import ru.arheo.core.db.ReportDistrictTable
import ru.arheo.core.db.ReportKeywordTable
import ru.arheo.core.db.ReportTable
import ru.arheo.core.domain.model.Monument
import ru.arheo.core.domain.model.DataReport

internal class DbReportSource(
    private val database: Database,
) : ReportSource {
    private suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) { transaction(database) { block() } }

    override suspend fun getAllReports(): List<DataReport> = dbQuery {
        loadAllReports()
    }

    override suspend fun getReportById(id: Long): DataReport? = dbQuery {
        loadReportById(id)
    }

    override suspend fun searchReports(query: String): List<DataReport> = dbQuery {
        if (query.isBlank()) return@dbQuery loadAllReports()
        val searchTerm = query.lowercase()
        val pattern = "%$searchTerm%"
        val matchingIds = findMatchingReportIds(pattern, searchTerm)
        matchingIds
            .mapNotNull { loadReportById(it) }
            .sortedByDescending { it.year }
    }

    override suspend fun addReport(report: DataReport): DataReport = dbQuery {
        val reportId = ReportTable.insertAndGetId {
            it[title] = report.title
            it[year] = report.year
            it[workType] = report.workType
            it[reportFilePath] = report.reportFilePath
            it[archiveFilePath] = report.archiveFilePath
        }.value
        insertRelatedData(reportId, report)
        loadReportById(reportId)!!
    }

    override suspend fun updateReport(report: DataReport): Unit = dbQuery {
        ReportTable.update({ ReportTable.id eq report.id }) {
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
        ReportTable.deleteWhere { ReportTable.id eq id }
    }

    override suspend fun getAllAuthors(): List<String> = dbQuery {
        ReportAuthorTable
            .selectAll()
            .map { it[ReportAuthorTable.author] }
            .distinct()
            .sorted()
    }

    override suspend fun getAllWorkTypes(): List<String> = dbQuery {
        ReportTable
            .selectAll()
            .map { it[ReportTable.workType] }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    override suspend fun getAllDistricts(): List<String> = dbQuery {
        ReportDistrictTable
            .selectAll()
            .map { it[ReportDistrictTable.district] }
            .distinct()
            .sorted()
    }

    override suspend fun getAllKeywords(): List<String> = dbQuery {
        ReportKeywordTable
            .selectAll()
            .map { it[ReportKeywordTable.keyword] }
            .distinct()
            .sorted()
    }

    private fun loadAllReports(): List<DataReport> =
        ReportTable
            .selectAll()
            .orderBy(ReportTable.year to SortOrder.DESC)
            .map { row -> buildReport(row) }

    private fun loadReportById(id: Long): DataReport? {
        val row = ReportTable
            .selectAll()
            .where { ReportTable.id eq id }
            .singleOrNull() ?: return null
        return buildReport(row)
    }

    private fun buildReport(row: ResultRow): DataReport {
        val reportId = row[ReportTable.id].value
        return DataReport(
            id = reportId,
            title = row[ReportTable.title],
            year = row[ReportTable.year],
            workType = row[ReportTable.workType],
            reportFilePath = row[ReportTable.reportFilePath],
            archiveFilePath = row[ReportTable.archiveFilePath],
            authors = loadAuthorsForReport(reportId),
            districts = loadDistrictsForReport(reportId),
            keywords = loadKeywordsForReport(reportId),
            monuments = loadMonumentsForReport(reportId),
        )
    }

    private fun loadAuthorsForReport(reportId: Long): List<String> =
        ReportAuthorTable
            .selectAll()
            .where { ReportAuthorTable.reportId eq reportId }
            .map { it[ReportAuthorTable.author] }

    private fun loadDistrictsForReport(reportId: Long): List<String> =
        ReportDistrictTable
            .selectAll()
            .where { ReportDistrictTable.reportId eq reportId }
            .map { it[ReportDistrictTable.district] }

    private fun loadKeywordsForReport(reportId: Long): List<String> =
        ReportKeywordTable
            .selectAll()
            .where { ReportKeywordTable.reportId eq reportId }
            .map { it[ReportKeywordTable.keyword] }

    private fun loadMonumentsForReport(reportId: Long): List<Monument> =
        MonumentTable
            .selectAll()
            .where { MonumentTable.reportId eq reportId }
            .map { row ->
                Monument(
                    id = row[MonumentTable.id].value,
                    name = row[MonumentTable.name],
                    type = row[MonumentTable.type],
                    culture = row[MonumentTable.culture],
                    period = row[MonumentTable.period],
                    geographicLocation = row[MonumentTable.geographicLocation],
                    number = row[MonumentTable.number],
                )
            }

    private fun findMatchingReportIds(pattern: String, searchTerm: String): Set<Long> {
        val ids = mutableSetOf<Long>()
        ReportTable.selectAll().where {
            (ReportTable.title.lowerCase() like pattern) or
                (ReportTable.workType.lowerCase() like pattern)
        }.forEach { ids.add(it[ReportTable.id].value) }
        ReportTable.selectAll().forEach { row ->
            if (row[ReportTable.year].toString().contains(searchTerm)) {
                ids.add(row[ReportTable.id].value)
            }
        }
        ReportAuthorTable.selectAll().where {
            ReportAuthorTable.author.lowerCase() like pattern
        }.forEach { ids.add(it[ReportAuthorTable.reportId]) }
        ReportDistrictTable.selectAll().where {
            ReportDistrictTable.district.lowerCase() like pattern
        }.forEach { ids.add(it[ReportDistrictTable.reportId]) }
        ReportKeywordTable.selectAll().where {
            ReportKeywordTable.keyword.lowerCase() like pattern
        }.forEach { ids.add(it[ReportKeywordTable.reportId]) }
        MonumentTable.selectAll().where {
            MonumentTable.name.lowerCase() like pattern
        }.forEach { ids.add(it[MonumentTable.reportId]) }
        return ids
    }

    private fun insertRelatedData(reportId: Long, report: DataReport) {
        report.authors.forEach { author ->
            ReportAuthorTable.insert {
                it[ReportAuthorTable.reportId] = reportId
                it[ReportAuthorTable.author] = author
            }
        }
        report.districts.forEach { district ->
            ReportDistrictTable.insert {
                it[ReportDistrictTable.reportId] = reportId
                it[ReportDistrictTable.district] = district
            }
        }
        report.keywords.forEach { keyword ->
            ReportKeywordTable.insert {
                it[ReportKeywordTable.reportId] = reportId
                it[ReportKeywordTable.keyword] = keyword
            }
        }
        report.monuments.forEach { monument ->
            MonumentTable.insert {
                it[MonumentTable.reportId] = reportId
                it[MonumentTable.name] = monument.name
                it[MonumentTable.type] = monument.type
                it[MonumentTable.culture] = monument.culture
                it[MonumentTable.period] = monument.period
                it[MonumentTable.geographicLocation] = monument.geographicLocation
                it[MonumentTable.number] = monument.number
            }
        }
    }

    private fun deleteRelatedData(reportId: Long) {
        ReportAuthorTable.deleteWhere { ReportAuthorTable.reportId eq reportId }
        ReportDistrictTable.deleteWhere { ReportDistrictTable.reportId eq reportId }
        ReportKeywordTable.deleteWhere { ReportKeywordTable.reportId eq reportId }
        MonumentTable.deleteWhere { MonumentTable.reportId eq reportId }
    }
}