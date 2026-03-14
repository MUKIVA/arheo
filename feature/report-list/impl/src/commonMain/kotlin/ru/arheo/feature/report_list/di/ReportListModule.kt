package ru.arheo.feature.report_list.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.arheo.feature.report_list.presentation.DefaultReportListComponent
import ru.arheo.feature.report_list.presentation.ReportListComponent
import ru.arheo.feature.report_list.presentation.ReportListStoreFactory

private const val REPORT_LIST_SCOPE = "ReportListScope"

val reportListModule = module {
    scope(named(REPORT_LIST_SCOPE)) {
        scoped { ReportListStoreFactory(get(), get()) }
    }
    factory<ReportListComponent.Factory> {
        val koin = getKoin()
        ReportListComponent.Factory { componentContext, output ->
            val scope = koin.createScope(
                "ReportList@${componentContext.hashCode()}",
                named(REPORT_LIST_SCOPE),
            )
            DefaultReportListComponent(
                componentContext = componentContext,
                reportListStoreFactory = scope.get(),
                output = output,
                koinScope = scope,
            )
        }
    }
}
