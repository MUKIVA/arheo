package ru.arheo.feature.report.list.di

import com.arkivanov.decompose.ComponentContext

data class FeatureDependencies(
    val componentContext: ComponentContext,
    val navigateCreateReport: () -> Unit,
    val navigateEditReport: (Long) -> Unit,
    val navigateViewReport: (Long) -> Unit
)