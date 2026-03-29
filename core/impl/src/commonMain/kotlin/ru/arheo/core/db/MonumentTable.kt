package ru.arheo.core.db

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object MonumentTable : LongIdTable("monuments") {
    val reportId = long("report_id").index()
    val name = text("name")
    val type = text("type")
    val culture = text("culture")
    val period = text("period")
    val geographicLocation = text("geographic_location")
    val number = text("number")
}
