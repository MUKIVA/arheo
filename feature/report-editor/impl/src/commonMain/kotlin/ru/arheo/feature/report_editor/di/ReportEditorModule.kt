package ru.arheo.feature.report_editor.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.arheo.feature.report_editor.presentation.DefaultReportEditorComponent
import ru.arheo.feature.report_editor.presentation.ReportEditorComponent
import ru.arheo.feature.report_editor.presentation.ReportEditorStoreFactory

private const val REPORT_EDITOR_SCOPE = "ReportEditorScope"

val reportEditorModule = module {
    scope(named(REPORT_EDITOR_SCOPE)) {
        scoped { ReportEditorStoreFactory(get(), get()) }
    }
    factory<ReportEditorComponent.Factory> {
        val koin = getKoin()
        ReportEditorComponent.Factory { componentContext, reportId, output ->
            val scope = koin.createScope(
                "ReportEditor@${componentContext.hashCode()}",
                named(REPORT_EDITOR_SCOPE),
            )
            DefaultReportEditorComponent(
                componentContext = componentContext,
                reportEditorStoreFactory = scope.get(),
                reportId = reportId,
                output = output,
                koinScope = scope,
            )
        }
    }
}
