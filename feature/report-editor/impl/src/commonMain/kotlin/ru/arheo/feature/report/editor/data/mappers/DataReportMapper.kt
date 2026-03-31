package ru.arheo.feature.report.editor.data.mappers

import ru.arheo.core.domain.model.MonumentData
import ru.arheo.core.domain.model.ReportData
import ru.arheo.feature.report.editor.domian.models.Author
import ru.arheo.feature.report.editor.domian.models.District
import ru.arheo.feature.report.editor.domian.models.Keyword
import ru.arheo.feature.report.editor.domian.models.monument.Monument
import ru.arheo.feature.report.editor.domian.models.monument.MonumentCulture
import ru.arheo.feature.report.editor.domian.models.monument.MonumentId
import ru.arheo.feature.report.editor.domian.models.monument.MonumentLocation
import ru.arheo.feature.report.editor.domian.models.monument.MonumentName
import ru.arheo.feature.report.editor.domian.models.monument.MonumentNumber
import ru.arheo.feature.report.editor.domian.models.monument.MonumentPeriod
import ru.arheo.feature.report.editor.domian.models.monument.MonumentType
import ru.arheo.feature.report.editor.domian.models.report.Report
import ru.arheo.feature.report.editor.domian.models.report.ReportId
import ru.arheo.feature.report.editor.domian.models.report.ReportName
import ru.arheo.feature.report.editor.domian.models.report.ReportWorkType
import ru.arheo.feature.report.editor.domian.models.report.ReportYear

@JvmInline
internal value class DataReportMapper(
    private val data: ReportData
) {
    fun toReport(): Report = Report(
        id = if (data.id != 0L) ReportId(data.id) else null,
        name = ReportName(data.title),
        year = ReportYear(data.year),
        authors = data.authors.map { Author(it) },
        workType = ReportWorkType(data.workType),
        districts = data.districts.map { District(it) },
        keywords = data.keywords.map { Keyword(it) },
        monuments = data.monuments.map { it.toDomain() },
        archiveFilePath = data.archiveFilePath,
    )

    private fun MonumentData.toDomain(): Monument = Monument(
        id = MonumentId(id),
        name = MonumentName(name),
        type = MonumentType(type),
        culture = MonumentCulture(culture),
        period = MonumentPeriod(period),
        geographicLocation = MonumentLocation(geographicLocation),
        number = MonumentNumber(number),
    )
}
