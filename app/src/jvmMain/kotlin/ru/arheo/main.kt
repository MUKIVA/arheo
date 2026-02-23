package ru.arheo

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.koin.core.context.startKoin
import ru.arheo.core.di.coreModule
import ru.arheo.feature.report_editor.di.reportEditorModule
import ru.arheo.feature.report_list.di.reportListModule
import ru.arheo.root.DefaultRootComponent
import ru.arheo.root.RootContent
import javax.swing.SwingUtilities

fun main() {
    val koin = startKoin {
        modules(coreModule, reportListModule, reportEditorModule)
    }.koin
    val lifecycle = LifecycleRegistry()
    val rootComponent = runOnUiThread {
        DefaultRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            koin = koin,
        )
    }
    lifecycle.onCreate()
    lifecycle.onStart()
    lifecycle.onResume()
    application {
        Window(
            onCloseRequest = {
                lifecycle.onDestroy()
                exitApplication()
            },
            title = "Arheo — Управление археологическими отчётами",
            state = rememberWindowState(width = 1200.dp, height = 800.dp),
        ) {
            MaterialTheme {
                RootContent(rootComponent)
            }
        }
    }
}

private fun <T> runOnUiThread(block: () -> T): T {
    if (SwingUtilities.isEventDispatchThread()) {
        return block()
    }
    var result: T? = null
    SwingUtilities.invokeAndWait { result = block() }
    @Suppress("UNCHECKED_CAST")
    return result as T
}
