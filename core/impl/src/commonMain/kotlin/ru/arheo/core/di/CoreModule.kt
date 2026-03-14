package ru.arheo.core.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.arheo.core.data.InMemoryReportRepository
import ru.arheo.core.data.ReportRepository

val coreModule = module {
    single<ReportRepository> { InMemoryReportRepository() }
    single<StoreFactory> { LoggingStoreFactory(DefaultStoreFactory()) }
}
