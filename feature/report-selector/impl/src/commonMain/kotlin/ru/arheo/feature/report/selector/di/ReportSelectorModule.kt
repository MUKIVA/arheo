package ru.arheo.feature.report.selector.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ru.arheo.feature.report.selector.data.DefaultFileRepository
import ru.arheo.feature.report.selector.domain.FileRepository
import ru.arheo.feature.report.selector.presentation.ReportSelectorStoreFactory

internal fun createFeatureModule(
    deps: FeatureDependencies
) = module {
    scope<FeatureScope> {
        scoped { deps.componentContext }
        scoped { ReportSelectorStoreFactory(get(), get()) }
        scoped { DefaultFileRepository(get()) }
            .bind<FileRepository>()
    }
}

