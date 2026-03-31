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
import org.koin.compose.koinInject
import org.koin.compose.scope.rememberKoinScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import ru.arheo.feature.report.selector.di.FeatureDependencies
import ru.arheo.feature.report.selector.di.FeatureScope
import ru.arheo.feature.report.selector.di.createFeatureModule
import ru.arheo.feature.report.selector.presentation.ReportSelectorComponent
import ru.arheo.feature.report.selector.presentation.ReportSelectorStore
import ru.arheo.feature.report.selector.ui.ReportSelectorRoot

internal class FeatureLauncher : ReportSelectorFeatureLauncher {

    @OptIn(KoinExperimentalAPI::class, KoinInternalApi::class)
    @Composable
    override fun launch(
        componentContext: ComponentContext,
        modifier: Modifier,
        archiveFilePath: String?,
        onFileStateChanged: (workingDirectory: String, hasFiles: Boolean) -> Unit,
    ) {
        val scope = rememberKoinScope(FeatureScope().scope)
        val deps = remember(componentContext, archiveFilePath) {
            FeatureDependencies(
                componentContext = componentContext,
                archiveFilePath = archiveFilePath,
            )
        }
        val module = remember { createFeatureModule(deps) }

        loadKoinModules(listOf(module))

        CompositionLocalProvider(
            LocalKoinScope provides ComposeContextWrapper(scope)
        ) {
            val component: ReportSelectorComponent = koinInject()

            LaunchedEffect(component) {
                component.state.collect { state ->
                    if (state is ReportSelectorStore.State.Content) {
                        onFileStateChanged(
                            state.workingDirectory,
                            state.attachedFiles.isNotEmpty(),
                        )
                    }
                }
            }

            ReportSelectorRoot(modifier)
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
