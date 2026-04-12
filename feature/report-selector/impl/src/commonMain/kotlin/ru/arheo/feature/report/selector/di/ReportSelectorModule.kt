package ru.arheo.feature.report.selector.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.arheo.feature.report.selector.data.DefaultFileRepository
import ru.arheo.feature.report.selector.domain.FileRepository
import ru.arheo.feature.report.selector.presentation.DefaultReportSelectorComponent
import ru.arheo.feature.report.selector.presentation.ReportSelectorComponent
import ru.arheo.feature.report.selector.presentation.ReportSelectorStoreFactory

internal fun createFeatureModule(
    deps: FeatureDependencies
) = module {
    scope<FeatureScope> {
        scoped { deps.componentContext }
        scoped { DefaultFileRepository(get()) }
            .bind<FileRepository>()
        scoped {
            ReportSelectorStoreFactory(
                storeFactory = get(),
                fileRepository = get(),
                working = deps.working,
                archive = deps.archive
            )
        }
        scoped {
            DefaultReportSelectorComponent(
                componentContext = get(),
                reportSelectorStoreFactory = get(),
            )
        }.bind<ReportSelectorComponent>()
    }
}
