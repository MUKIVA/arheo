package ru.arheo.feature.report.editor.ui.models

import androidx.compose.runtime.Stable

@Stable
internal interface DynamicFieldActions {
    fun onAdd(value: String)
    fun onRemove(value: String)
}