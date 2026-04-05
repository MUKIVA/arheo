package ru.arheo.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import org.koin.compose.koinInject
import ru.arheo.feature.report.editor.launchReportEditorContent
import ru.arheo.feature.report.list.launchReportListContent
import ru.arheo.feature.report.viewer.launchReportViewerContent
import ru.arheo.navigation.RootComponent


private val screenModifier: Modifier
    get() = Modifier
        .fillMaxSize()
        .widthIn(max = 1200.dp)
        .padding(horizontal = 32.dp)

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
                onCreateReport = instance.navigateCreateReport,
                onEditReport = instance.navigateEditReport,
                onViewReport = instance.navigateViewReport,
                modifier = screenModifier

            )
            is RootComponent.Child.ReportEditor -> launchReportEditorContent(
                componentContext = instance.componentContext,
                reportId = instance.reportId,
                navigateBack = instance.navigateBack,
                modifier = screenModifier
            )
            is RootComponent.Child.ReportViewer -> launchReportViewerContent(
                componentContext = instance.componentContext,
                reportId = instance.reportId,
                navigateBack = instance.navigateBack,
                modifier = screenModifier
            )
        }
    }
}
