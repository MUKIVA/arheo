package ru.arheo.feature.report.editor.presentation.models

import androidx.compose.runtime.Immutable
import ru.arheo.feature.report.editor.domian.models.Author
import ru.arheo.feature.report.editor.domian.models.District
import ru.arheo.feature.report.editor.domian.models.Keyword
import ru.arheo.feature.report.editor.domian.models.monument.Monument
import ru.arheo.feature.report.editor.domian.models.report.ReportId
import ru.arheo.feature.report.editor.domian.models.report.ReportName
import ru.arheo.feature.report.editor.domian.models.report.ReportWorkType
import ru.arheo.feature.report.editor.domian.models.report.ReportYear

@Immutable
internal data class UiReport(
    val id: ReportId,
    val name: ReportName,
    val year: ReportYear,
    val authors: List<Author>,
    val workType: ReportWorkType,
    val districts: List<District>,
    val keywords: List<Keyword>,
    val monuments: List<UiMonument>
)