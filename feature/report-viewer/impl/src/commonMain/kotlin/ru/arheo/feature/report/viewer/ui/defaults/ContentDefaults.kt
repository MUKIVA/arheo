package ru.arheo.feature.report.viewer.ui.defaults

import arheo.feature.report_viewer.impl.generated.resources.*
import ru.arheo.feature.report.viewer.presentation.models.UiMonument
import ru.arheo.feature.report.viewer.presentation.models.UiReport
import ru.arheo.feature.report.viewer.ui.models.MainInfoRow
import ru.arheo.feature.report.viewer.ui.models.MonumentInfoRow

internal object ContentDefaults {

    fun buildMainInfo(
        report: UiReport?,
    ): List<MainInfoRow> {
        return listOf(
            MainInfoRow(
                label = Res.string.main_info_year_field,
                value = formatYear(report?.year)
            ),
            MainInfoRow(
                label = Res.string.main_info_authors_field,
                value = report?.authors ?: "-"
            ),
            MainInfoRow(
                label = Res.string.main_info_work_type_field,
                value = report?.workType ?: "-"
            ),
            MainInfoRow(
                label = Res.string.main_info_districts_field,
                value = report?.districts ?: "-"
            ),
            MainInfoRow(
                label = Res.string.main_info_keywords_field,
                value = report?.keywords ?: "-"
            ),
        )
    }

    fun buildMonumentInfo(
        monument: UiMonument.Item?
    ): List<MonumentInfoRow> {
        return listOf(
            MonumentInfoRow(
                label = Res.string.monument_list_item_name_field,
                value = monument?.name ?: "-"
            ),
            MonumentInfoRow(
                label = Res.string.monument_list_item_type_field,
                value = monument?.type ?: "-"
            ),
            MonumentInfoRow(
                label = Res.string.monument_list_item_culture_field,
                value = monument?.culture ?: "-"
            ),
            MonumentInfoRow(
                label = Res.string.monument_list_item_period_field,
                value = monument?.period ?: "-"
            ),
            MonumentInfoRow(
                label = Res.string.monument_list_item_location_field,
                value = monument?.location ?: "-"
            ),
            MonumentInfoRow(
                label = Res.string.monument_list_item_number_field,
                value = monument?.number ?: "-"
            ),
        )
    }
}

private fun formatYear(year: Int?): String {
    if (year == null || year == 0) {
        return "-"
    }
    return year.toString()
}