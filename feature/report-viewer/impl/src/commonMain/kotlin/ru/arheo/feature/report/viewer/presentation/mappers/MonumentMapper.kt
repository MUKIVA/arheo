package ru.arheo.feature.report.viewer.presentation.mappers

import ru.arheo.feature.report.viewer.domain.models.monument.Monument
import ru.arheo.feature.report.viewer.presentation.models.UiMonument

@JvmInline
internal value class MonumentMapper(
    private val monument: Monument
) {
    fun toUiContent(): UiMonument {
        return UiMonument.Item(
            id = monument.id.value,
            name =     formatText(monument.name.value),
            type =     formatText(monument.type.value),
            culture =  formatText(monument.culture.value),
            period =   formatText(monument.period.value),
            location = formatText(monument.location.value),
            number =   formatText(monument.number.value)
        )
    }

    private fun formatText(text: String): String {
        if (text.isBlank()) {
            return "-"
        }
        return text
    }
}