package ru.arheo.di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.arheo.feature.report.selector.ReportSelectorFeatureLauncher
import ru.arheo.feature.report.selector.createReportSelectorFeatureLauncher
import ru.arheo.navigation.DefaultRootComponent
import ru.arheo.navigation.RootComponent

internal fun createAppModule() = module {
    single<LifecycleRegistry> { LifecycleRegistry() }
        .bind<Lifecycle>()
    single<ComponentContext> { DefaultComponentContext(get()) }
    single<RootComponent> {
        DefaultRootComponent(get())
    }
    single<StoreFactory> { LoggingStoreFactory(DefaultStoreFactory()) }

    factory<ReportSelectorFeatureLauncher> {
        createReportSelectorFeatureLauncher()
    }
}