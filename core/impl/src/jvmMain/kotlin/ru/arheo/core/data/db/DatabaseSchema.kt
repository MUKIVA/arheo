package ru.arheo.core.data.db

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object ReportsTable : LongIdTable("reports") {
    val title = text("title")
    val year = integer("year")
    val workType = text("work_type")
    val reportFilePath = text("report_file_path").nullable()
    val archiveFilePath = text("archive_file_path").nullable()
}

object ReportAuthorsTable : Table("report_authors") {
    val reportId = long("report_id").index()
    val author = text("author")
}

object ReportDistrictsTable : Table("report_districts") {
    val reportId = long("report_id").index()
    val district = text("district")
}

object ReportKeywordsTable : Table("report_keywords") {
    val reportId = long("report_id").index()
    val keyword = text("keyword")
}

object MonumentsTable : LongIdTable("monuments") {
    val reportId = long("report_id").index()
    val name = text("name")
    val type = text("type")
    val culture = text("culture")
    val period = text("period")
    val geographicLocation = text("geographic_location")
    val number = text("number")
}
