package ru.arheo.feature.report.list.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.arheo.feature.report.list.data.DefaultReportRepository
import ru.arheo.feature.report.list.domain.ReportRepository
import ru.arheo.feature.report.list.presentation.DefaultReportListComponent
import ru.arheo.feature.report.list.presentation.ReportListComponent
import ru.arheo.feature.report.list.presentation.ReportListStoreFactory

internal fun createFeatureModule(
    deps: FeatureDependencies
) = module {

    scope<FeatureScope> {
        scoped { deps.componentContext }
        scoped { DefaultReportRepository(get()) }
            .bind<ReportRepository>()
        scoped { ReportListStoreFactory(get(), get()) }
        scoped {
            DefaultReportListComponent(
                componentContext = get(),
                reportListStoreFactory = get(),
                navigateCreateReport = deps.navigateCreateReport,
                navigateEditReport = deps.navigateEditReport
            )
        }.bind<ReportListComponent>()
    }
}
