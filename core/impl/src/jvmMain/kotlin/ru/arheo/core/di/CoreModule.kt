package ru.arheo.core.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.dsl.module
import ru.arheo.core.data.DefaultFileManager
import ru.arheo.core.data.FileManager
import ru.arheo.core.data.ReportRepository
import ru.arheo.core.data.SqliteReportRepository
import ru.arheo.core.data.db.DatabaseFactory

val coreModule = module {
    single<Database> { DatabaseFactory.create() }
    single<ReportRepository> { SqliteReportRepository(get()) }
    single<FileManager> { DefaultFileManager() }
    single<StoreFactory> { LoggingStoreFactory(DefaultStoreFactory()) }
}
