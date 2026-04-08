package ru.arheo.core.db

import org.jetbrains.exposed.v1.core.Table

object ReportAuthorTable : Table("report_author") {
    val reportId = long("report_id").index()
    val author = text("author")
}