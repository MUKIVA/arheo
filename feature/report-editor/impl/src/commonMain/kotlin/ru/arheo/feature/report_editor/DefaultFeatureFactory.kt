package ru.arheo.feature.report_editor

import com.arkivanov.decompose.ComponentContext
import ru.arheo.feature.report_editor.presentation.DefaultReportEditorComponent
import ru.arheo.feature.report_editor.presentation.ReportEditorComponent

internal class DefaultFeatureFactory {

    fun createComponent(
        componentContext: ComponentContext,
    ): ReportEditorComponent {
        return DefaultReportEditorComponent(
            componentContext = componentContext
        )
    }
}

fun createDefaultReportEditorComponent(
    componentContext: ComponentContext
): ReportEditorComponent {
    return DefaultFeatureFactory().createComponent(componentContext)
}

