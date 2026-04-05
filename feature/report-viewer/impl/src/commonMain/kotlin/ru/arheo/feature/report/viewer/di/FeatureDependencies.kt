package ru.arheo.feature.report.viewer.di

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.ComponentContext

@Immutable
internal data class FeatureDependencies(
    val componentContext: ComponentContext,
    val reportId: Long,
    val navigateBack: () -> Unit
)