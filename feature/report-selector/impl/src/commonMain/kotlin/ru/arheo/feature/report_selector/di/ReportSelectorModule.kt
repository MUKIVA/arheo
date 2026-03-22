package ru.arheo.feature.report_selector.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.arheo.feature.report_selector.presentation.DefaultReportSelectorComponent
import ru.arheo.feature.report_selector.presentation.ReportSelectorComponent
import ru.arheo.feature.report_selector.presentation.ReportSelectorStoreFactory

private const val REPORT_SELECTOR_SCOPE = "ReportSelectorScope"

val reportSelectorModule = module {
    scope(named(REPORT_SELECTOR_SCOPE)) {
        scoped { ReportSelectorStoreFactory(get(), get()) }
    }
    factory<ReportSelectorComponent.Factory> {
        val koin = getKoin()
        ReportSelectorComponent.Factory { componentContext ->
            val scope = koin.createScope(
                "ReportSelector@${componentContext.hashCode()}",
                named(REPORT_SELECTOR_SCOPE),
            )
            DefaultReportSelectorComponent(
                componentContext = componentContext,
                reportSelectorStoreFactory = scope.get(),
                koinScope = scope,
            )
        }
    }
}
