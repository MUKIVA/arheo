package ru.arheo.feature.report.editor.data.mappers

import ru.arheo.core.domain.model.MonumentData
import ru.arheo.core.domain.model.ReportData
import ru.arheo.feature.report.editor.domian.models.monument.Monument
import ru.arheo.feature.report.editor.domian.models.report.Report

@JvmInline
internal value class DomainReportMapper(
    private val report: Report
) {
    fun toReportData(): ReportData = ReportData(
        id = report.id?.value ?: 0L,
        title = report.name.value,
        year = report.year.value,
        authors = report.authors.map { it.value },
        workType = report.workType.value,
        districts = report.districts.map { it.value },
        keywords = report.keywords.map { it.value },
        monuments = report.monuments.map { it.toData() },
        archiveFilePath = report.archive.toString(),
    )

    private fun Monument.toData(): MonumentData = MonumentData(
        id = id.value,
        name = name.value,
        type = type.value,
        culture = culture.value,
        period = period.value,
        geographicLocation = geographicLocation.value,
        number = number.value
    )
}
