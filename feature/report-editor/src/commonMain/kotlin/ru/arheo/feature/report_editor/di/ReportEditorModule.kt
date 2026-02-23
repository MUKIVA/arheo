package ru.arheo.feature.report_editor.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.arheo.feature.report_editor.presentation.ReportEditorStoreFactory

val reportEditorModule = module {
    factoryOf(::ReportEditorStoreFactory)
}
