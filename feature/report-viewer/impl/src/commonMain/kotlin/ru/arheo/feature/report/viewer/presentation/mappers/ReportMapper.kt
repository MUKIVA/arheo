package ru.arheo.feature.report.viewer.presentation.mappers

import ru.arheo.feature.report.viewer.domain.models.Author
import ru.arheo.feature.report.viewer.domain.models.District
import ru.arheo.feature.report.viewer.domain.models.Keyword
import ru.arheo.feature.report.viewer.domain.models.report.Report
import ru.arheo.feature.report.viewer.presentation.models.UiReport

@JvmInline
internal value class ReportMapper(
    private val report: Report
) {

    fun toUi(): UiReport {
        return UiReport(
            id = report.id.value,
            name = formatText(report.name.value),
            year = report.year.year,
            authors = formatList(
                items = report.authors.map(Author::name)
            ),
            workType = formatText(report.workType.value),
            districts = formatList(
                items = report.districts.map(District::name)
            ),
            keywords = formatList(
                items = report.keywords.map(Keyword::name)
            ),
        )
    }
}

private fun formatList(items: List<String>): String {
    if (items.isEmpty()) {
        return "-"
    }
    return items.joinToString(", ")
}

private fun formatText(text: String): String {
    if (text.isBlank()) {
        return "-"
    }
    return text
}