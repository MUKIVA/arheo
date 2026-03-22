package ru.arheo.core.data.db

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import ru.arheo.core.util.AppPaths

object DatabaseFactory {

    fun create(): Database {
        val dbPath = AppPaths.resolveDatabasePath()
        val database = Database.connect(
            url = "jdbc:sqlite:$dbPath",
            driver = "org.sqlite.JDBC",
        )
        transaction(database) {
            SchemaUtils.create(
                ReportsTable,
                ReportAuthorsTable,
                ReportDistrictsTable,
                ReportKeywordsTable,
                MonumentsTable,
            )
        }
        return database
    }
}
