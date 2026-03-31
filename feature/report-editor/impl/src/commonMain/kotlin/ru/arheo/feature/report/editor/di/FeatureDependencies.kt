package ru.arheo.feature.report.editor.di

import com.arkivanov.decompose.ComponentContext

internal data class FeatureDependencies(
    val componentContext: ComponentContext,
    val reportId: Long? = null,
    val navigateBack: () -> Unit
)