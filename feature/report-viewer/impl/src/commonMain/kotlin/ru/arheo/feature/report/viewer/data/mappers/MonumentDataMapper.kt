package ru.arheo.feature.report.viewer.data.mappers

import ru.arheo.core.domain.model.MonumentData
import ru.arheo.feature.report.viewer.domain.models.monument.Monument
import ru.arheo.feature.report.viewer.domain.models.monument.MonumentCulture
import ru.arheo.feature.report.viewer.domain.models.monument.MonumentId
import ru.arheo.feature.report.viewer.domain.models.monument.MonumentLocation
import ru.arheo.feature.report.viewer.domain.models.monument.MonumentName
import ru.arheo.feature.report.viewer.domain.models.monument.MonumentNumber
import ru.arheo.feature.report.viewer.domain.models.monument.MonumentPeriod
import ru.arheo.feature.report.viewer.domain.models.monument.MonumentType

@JvmInline
internal value class MonumentDataMapper(
    private val data: MonumentData
) {
    fun toDomain(): Monument {
        return Monument(
            id = MonumentId(data.id),
            name = MonumentName(data.name),
            type = MonumentType(data.type),
            culture = MonumentCulture(data.culture),
            period = MonumentPeriod(data.culture),
            location = MonumentLocation(data.geographicLocation),
            number = MonumentNumber(data.number)
        )
    }
}