package ru.arheo.feature.report.editor.domian.models.report

import ru.arheo.feature.report.editor.domian.models.Author
import ru.arheo.feature.report.editor.domian.models.District
import ru.arheo.feature.report.editor.domian.models.Keyword

internal data class Report(
    val id: ReportId?,
    val name: ReportName,
    val year: ReportYear,
    val authors: List<Author>,
    val workType: ReportWorkType,
    val districts: List<District>,
    val keywords: List<Keyword>
) {
    companion object {
        fun default() = Report(
            id = null,
            name = ReportName(String()),
            year = ReportYear(0),
            authors = emptyList(),
            workType = ReportWorkType(String()),
            districts = emptyList(),
            keywords = emptyList()
        )
    }
}

