package ru.arheo.feature.report_list.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.arheo.feature.report_list.presentation.ReportListStoreFactory

val reportListModule = module {
    factoryOf(::ReportListStoreFactory)
}
