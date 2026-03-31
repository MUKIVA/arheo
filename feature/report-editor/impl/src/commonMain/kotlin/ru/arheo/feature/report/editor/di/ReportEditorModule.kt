package ru.arheo.feature.report.editor.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.arheo.feature.report.editor.data.DefaultFileRepository
import ru.arheo.feature.report.editor.data.DefaultReportRepository
import ru.arheo.feature.report.editor.domian.FileRepository
import ru.arheo.feature.report.editor.domian.ReportRepository
import ru.arheo.feature.report.editor.presentation.DefaultReportEditorComponent
import ru.arheo.feature.report.editor.presentation.ReportEditorComponent
import ru.arheo.feature.report.editor.presentation.ReportEditorStoreFactory

internal fun createFeatureModule(
    deps: FeatureDependencies
) = module {
    scope<FeatureScope> {
        scoped { deps.componentContext }
        scoped { DefaultReportRepository(get()) }
            .bind<ReportRepository>()
        scoped { DefaultFileRepository(get()) }
            .bind<FileRepository>()

        scoped {
            ReportEditorStoreFactory(
                storeFactory = get(),
                reportRepository = get(),
                fileRepository = get()
            )
        }

        scoped {
            DefaultReportEditorComponent(
                componentContext = get(),
                reportId = deps.reportId,
                reportEditorStoreFactory = get(),
                navigateBack = deps.navigateBack
            )
        }.bind<ReportEditorComponent>()
    }

}
