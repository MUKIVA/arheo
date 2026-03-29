package ru.arheo.feature.report.selector

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext

interface ReportSelectorFeatureLauncher {

    @Composable
    fun launch(
        componentContext: ComponentContext,
        modifier: Modifier = Modifier
    )

}