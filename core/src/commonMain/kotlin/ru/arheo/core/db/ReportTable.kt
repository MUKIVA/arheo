package ru.arheo.core.db

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object ReportTable : LongIdTable("report") {
    val title = text("title")
    val year = integer("year")
    val workType = text("work_type")
    val archiveFilePath = text("archive_file_path").nullable()
}