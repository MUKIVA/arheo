package ru.arheo.feature.report.editor

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
import ru.arheo.feature.report.editor.di.FeatureDependencies
import ru.arheo.feature.report.editor.di.FeatureScope
import ru.arheo.feature.report.editor.di.createFeatureModule
import ru.arheo.feature.report.editor.ui.ReportEditorRoot

@OptIn(KoinExperimentalAPI::class, KoinInternalApi::class)
@Composable
fun launchReportEditorContent(
    componentContext: ComponentContext,
    reportId: Long?,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {}
) {
    val scope = rememberKoinScope(FeatureScope().scope)
    val deps = remember {
        FeatureDependencies(
            componentContext = componentContext,
            reportId = reportId,
            navigateBack = navigateBack
        )
    }
    val module = remember { createFeatureModule(deps) }

    loadKoinModules(listOf(module))

    CompositionLocalProvider(
        LocalKoinScope provides ComposeContextWrapper(scope)
    ) {
        ReportEditorRoot(modifier)
    }

    DisposableEffect(Unit) {
        onDispose {
            unloadKoinModules(listOf(module))
            scope.close()
        }
    }
}