package ru.arheo.feature.report.viewer.data.mappers

import ru.arheo.core.domain.model.ReportData
import ru.arheo.feature.report.viewer.domain.models.Author
import ru.arheo.feature.report.viewer.domain.models.District
import ru.arheo.feature.report.viewer.domain.models.Keyword
import ru.arheo.feature.report.viewer.domain.models.report.Report
import ru.arheo.feature.report.viewer.domain.models.report.ReportId
import ru.arheo.feature.report.viewer.domain.models.report.ReportName
import ru.arheo.feature.report.viewer.domain.models.report.ReportWorkType
import ru.arheo.feature.report.viewer.domain.models.report.ReportYear
import java.nio.file.Path

@JvmInline
internal value class ReportDataMapper(
    private val data: ReportData
) {

    fun toDomain(): Report {
        return Report(
            id = ReportId(data.id),
            name = ReportName(data.title),
            year = ReportYear(data.year),
            authors = data.authors.map(::Author),
            workType = ReportWorkType(data.workType),
            districts = data.districts.map(::District),
            keywords = data.keywords.map(::Keyword),
            monuments = data.monuments.map { monumentData ->
                DataMapper.from(monumentData).toDomain()
            },
            archive = data.archiveFilePath?.let { Path.of(it) },
        )
    }

}