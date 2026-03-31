package ru.arheo.feature.report.selector

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
import ru.arheo.feature.report.selector.di.FeatureDependencies
import ru.arheo.feature.report.selector.di.FeatureScope
import ru.arheo.feature.report.selector.di.createFeatureModule
import ru.arheo.feature.report.selector.ui.ReportSelectorContent

internal class FeatureLauncher : ReportSelectorFeatureLauncher {

    @OptIn(KoinExperimentalAPI::class, KoinInternalApi::class)
    @Composable
    override fun launch(
        componentContext: ComponentContext,
        modifier: Modifier
    ) {
        val scope = rememberKoinScope(FeatureScope().scope)
        val deps = remember(componentContext) {
            FeatureDependencies(
                componentContext = componentContext
            )
        }
        val module = remember { createFeatureModule(deps) }

        loadKoinModules(listOf(module))

        CompositionLocalProvider(
            LocalKoinScope provides ComposeContextWrapper(scope)
        ) {
            ReportSelectorContent(modifier)
        }

        DisposableEffect(Unit) {
            onDispose {
                unloadKoinModules(listOf(module))
                scope.close()
            }
        }
    }

}

fun createReportSelectorFeatureLauncher(): ReportSelectorFeatureLauncher {
    return FeatureLauncher()
}