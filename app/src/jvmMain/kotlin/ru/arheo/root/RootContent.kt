package ru.arheo.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.arheo.feature.report_editor.ui.ReportEditorContent
import ru.arheo.feature.report_list.ui.ReportListContent

@Composable
fun RootContent(component: RootComponent) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.ReportListChild -> ReportListContent(instance.component)
            is RootComponent.Child.ReportEditorChild -> ReportEditorContent(instance.component)
        }
    }
}
