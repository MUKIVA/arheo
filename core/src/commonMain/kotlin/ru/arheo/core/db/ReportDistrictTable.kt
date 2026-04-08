package ru.arheo.core.db

import org.jetbrains.exposed.v1.core.Table

object ReportDistrictTable : Table("report_district") {
    val reportId = long("report_id").index()
    val district = text("district")
}