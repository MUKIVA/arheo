package ru.arheo.feature.report.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import org.koin.compose.ComposeContextWrapper
import org.koin.compose.LocalKoinScope
import org.koin.compose.scope.rememberKoinScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import ru.arheo.feature.report.list.di.FeatureDependencies
import ru.arheo.feature.report.list.di.FeatureScope
import ru.arheo.feature.report.list.di.createFeatureModule
import ru.arheo.feature.report.list.ui.ReportListRoot

@OptIn(KoinInternalApi::class, KoinExperimentalAPI::class)
@Composable
fun launchReportListContent(
    componentContext: ComponentContext,
    modifier: Modifier = Modifier,
    onEditReport: (Long) -> Unit = {},
    onCreateReport: () -> Unit = {}
) {
    val scope = rememberKoinScope(FeatureScope().scope)
    val deps = remember(componentContext, onEditReport, onCreateReport) {
        FeatureDependencies(
            componentContext = componentContext,
            navigateCreateReport = onCreateReport,
            navigateEditReport = onEditReport
        )
    }
    val module = remember { createFeatureModule(deps) }

    LaunchedEffect(Unit) {
        loadKoinModules(
            modules = listOf(module)
        )
    }

    CompositionLocalProvider(
        LocalKoinScope provides ComposeContextWrapper(scope)
    ) {
        ReportListRoot(modifier)
    }

    DisposableEffect(Unit) {
        onDispose {
            unloadKoinModules(listOf(module))
            scope.close()
        }
    }
}