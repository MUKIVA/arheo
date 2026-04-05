package ru.arheo.feature.report.viewer.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
internal sealed class UiMonument(val key: Long) {

    @Immutable
    data object Header : UiMonument(-1)

    @Immutable
    data class Item(
        val id: Long,
        val name: String,
        val type: String,
        val culture: String,
        val period: String,
        val location: String,
        val number: String,
    ) : UiMonument(id)

}