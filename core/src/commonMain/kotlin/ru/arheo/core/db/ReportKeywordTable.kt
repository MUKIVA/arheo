package ru.arheo.core.db

import org.jetbrains.exposed.v1.core.Table

object ReportKeywordTable : Table("report_keyword") {
    val reportId = long("report_id").index()
    val keyword = text("keyword")
}