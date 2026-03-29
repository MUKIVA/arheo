package ru.arheo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import org.koin.compose.koinInject
import ru.arheo.feature.report.list.launchReportListContent
import ru.arheo.navigation.RootComponent

@Composable
fun RootContent(
    modifier: Modifier = Modifier,
    component: RootComponent = koinInject()
) {
    Children(
        modifier = modifier,
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.ReportList -> launchReportListContent(
                componentContext = instance.componentContext,
                modifier = Modifier.fillMaxSize()
            )
            is RootComponent.Child.ReportEditor -> Box(Modifier.fillMaxSize())
        }
    }
}
