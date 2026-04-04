package ru.arheo.feature.report.editor.ui.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import org.jetbrains.compose.resources.StringResource

@Immutable
internal data class DynamicFieldState(
    val labelRes: StringResource,
    val placeholderRes: StringResource,
    val emptyMessageRes: StringResource,
    val data: State<List<String>>,
    val actions: DynamicFieldActions
)