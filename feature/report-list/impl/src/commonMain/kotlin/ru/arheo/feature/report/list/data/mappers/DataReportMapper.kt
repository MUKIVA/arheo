package ru.arheo.feature.report.list.data.mappers

import ru.arheo.core.domain.model.DataReport
import ru.arheo.feature.report.list.domain.models.Report

@JvmInline
internal value class DataReportMapper(
    private val data: DataReport
) {
    fun toReport(): Report {
        return Report(
            id = data.id,
            name = data.title,
            year = data.year,
            authors = data.authors,
            workType = data.workType
        )
    }
}