package ru.arheo.feature.report.selector.di

import com.arkivanov.decompose.ComponentContext
import java.nio.file.Path

internal data class FeatureDependencies(
    val componentContext: ComponentContext,
    val working: Path,
    val archive: Path? = null,
)