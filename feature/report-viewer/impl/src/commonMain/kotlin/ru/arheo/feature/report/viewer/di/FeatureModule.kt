package ru.arheo.feature.report.viewer.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.arheo.feature.report.viewer.data.DefaultReportRepository
import ru.arheo.feature.report.viewer.data.SystemFileRepository
import ru.arheo.feature.report.viewer.domain.FileRepository
import ru.arheo.feature.report.viewer.domain.ReportRepository
import ru.arheo.feature.report.viewer.presentation.DefaultReportViewerComponent
import ru.arheo.feature.report.viewer.presentation.ReportViewerComponent
import ru.arheo.feature.report.viewer.presentation.ReportViewerStoreFactory

internal fun createFeatureModule(
    deps: FeatureDependencies
) = module {
    scope<FeatureScope> {
        scoped { deps.componentContext }
        scoped {
            DefaultReportViewerComponent(
                componentContext = get(),
                reportViewerStoreFactory = get(),
                navigateBack = deps.navigateBack
            )
        }.bind<ReportViewerComponent>()

        scoped {
            ReportViewerStoreFactory(
                storeFactory = get(),
                reportId = deps.reportId,
                reportRepository = get(),
                fileRepository = get()
            )
        }
        scoped { DefaultReportRepository(get()) }
            .bind<ReportRepository>()
        scoped { SystemFileRepository(get()) }
            .bind<FileRepository>()
    }
}