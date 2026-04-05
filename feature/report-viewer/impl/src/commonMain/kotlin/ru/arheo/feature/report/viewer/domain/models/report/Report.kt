package ru.arheo.feature.report.viewer.domain.models.report

import ru.arheo.feature.report.viewer.domain.models.Author
import ru.arheo.feature.report.viewer.domain.models.District
import ru.arheo.feature.report.viewer.domain.models.Keyword
import ru.arheo.feature.report.viewer.domain.models.monument.Monument

internal data class Report(
    val id: ReportId,
    val name: ReportName,
    val year: ReportYear,
    val authors: List<Author>,
    val workType: ReportWorkType,
    val districts: List<District>,
    val keywords: List<Keyword>,
    val monuments: List<Monument>
)