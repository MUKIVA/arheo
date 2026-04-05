package ru.arheo.feature.report.viewer.ui.models

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource

@Immutable
internal data class MainInfoRow(
    val label: StringResource,
    val value: String
)