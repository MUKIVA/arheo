package ru.arheo.feature.report.editor.ui.models

internal fun interface DynamicFieldDataProvider {
    fun provide(): List<String>
}