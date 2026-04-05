package ru.arheo.feature.report.viewer.data.mappers

import ru.arheo.core.domain.model.ReportData
import ru.arheo.feature.report.viewer.domain.models.Report
import ru.arheo.feature.report.viewer.domain.models.ReportId

@JvmInline
internal value class ReportDataMapper(
    private val data: ReportData
) {

    fun toDomain(): Report {
        return Report(
            id = ReportId(data.id)
        )
    }

}