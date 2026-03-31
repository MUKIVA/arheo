package ru.arheo.feature.report.editor.presentation.models

import androidx.compose.runtime.Immutable
import ru.arheo.feature.report.editor.domian.models.monument.MonumentCulture
import ru.arheo.feature.report.editor.domian.models.monument.MonumentId
import ru.arheo.feature.report.editor.domian.models.monument.MonumentLocation
import ru.arheo.feature.report.editor.domian.models.monument.MonumentName
import ru.arheo.feature.report.editor.domian.models.monument.MonumentNumber
import ru.arheo.feature.report.editor.domian.models.monument.MonumentPeriod
import ru.arheo.feature.report.editor.domian.models.monument.MonumentType

@Immutable
internal data class UiMonument(
    val id: MonumentId,
    val name: MonumentName,
    val type: MonumentType,
    val culture: MonumentCulture,
    val period: MonumentPeriod,
    val geographicLocation: MonumentLocation,
    val number: MonumentNumber
) {
    companion object {
        fun default() = UiMonument(
            id = MonumentId(0),
            name = MonumentName(String()),
            type = MonumentType(String()),
            culture = MonumentCulture(String()),
            period = MonumentPeriod(String()),
            geographicLocation = MonumentLocation(String()),
            number = MonumentNumber(String())
        )
    }
}