package ru.arheo.feature.report.selector

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import java.nio.file.Path

interface ReportSelectorFeatureLauncher {

    @Composable
    fun launch(
        componentContext: ComponentContext,
        modifier: Modifier,
        working: Path,
        archive: Path? = null
    )
}
