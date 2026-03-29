package ru.arheo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.stringResource
import org.koin.core.Koin
import org.koin.core.context.startKoin
import ru.arheo.app.generated.resources.Res
import ru.arheo.app.generated.resources.app_name
import ru.arheo.core.di.coreModule
import ru.arheo.di.createAppModule
import ru.arheo.feature.report_editor.di.reportEditorModule
import ru.arheo.ui.App
import ru.arheo.ui.theme.ArheoTheme

fun main() = runBlocking {
    val koin = initDiGraph()

    val lifecycle = koin.get<LifecycleRegistry>()

    lifecycle.init()

    application {
        ArheoTheme {
            Window(
                onCloseRequest = {
                    lifecycle.destroy()
                    exitApplication()
                },
                title = stringResource(Res.string.app_name),
                state = rememberWindowState(width = 1200.dp, height = 800.dp),
                content = { App(Modifier.fillMaxSize()) }
            )
        }
    }
}

private fun initDiGraph(): Koin {
    return startKoin {
        modules(
            coreModule,
            createAppModule(),
            reportEditorModule,
        )
    }.koin
}

private fun LifecycleRegistry.init() {
    onCreate()
    onStart()
    onResume()
}

private fun LifecycleRegistry.destroy() {
    onPause()
    onStop()
    onDestroy()
}