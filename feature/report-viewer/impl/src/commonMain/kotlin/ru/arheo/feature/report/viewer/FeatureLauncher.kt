package ru.arheo.feature.report.viewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
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
import ru.arheo.feature.report.viewer.di.FeatureDependencies
import ru.arheo.feature.report.viewer.di.FeatureScope
import ru.arheo.feature.report.viewer.di.createFeatureModule
import ru.arheo.feature.report.viewer.ui.ReportViewerRoot

@OptIn(KoinExperimentalAPI::class, KoinInternalApi::class)
@Composable
fun launchReportViewerContent(
    componentContext: ComponentContext,
    reportId: Long,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberKoinScope(FeatureScope().scope)
    val deps = remember(componentContext) {
        FeatureDependencies(componentContext, reportId, navigateBack)
    }
    val module = remember { createFeatureModule(deps) }

    loadKoinModules(modules = listOf(module))

    CompositionLocalProvider(
        LocalKoinScope provides ComposeContextWrapper(scope)
    ) {
        ReportViewerRoot(modifier)
    }

    DisposableEffect(Unit) {
        onDispose {
            unloadKoinModules(listOf(module))
            scope.close()
        }
    }
}