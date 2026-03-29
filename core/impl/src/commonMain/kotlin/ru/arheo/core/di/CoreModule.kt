package ru.arheo.core.di

import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.dsl.module
import ru.arheo.core.data.DbReportSource
import ru.arheo.core.data.DefaultFileSource
import ru.arheo.core.data.FileSource
import ru.arheo.core.data.ReportSource
import ru.arheo.core.db.DatabaseFactory

val coreModule = module {
    single<Database> { DatabaseFactory.create() }
    single<ReportSource> { DbReportSource(get()) }
    single<FileSource> { DefaultFileSource() }
}
