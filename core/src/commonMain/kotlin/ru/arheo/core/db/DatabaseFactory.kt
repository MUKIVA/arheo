package ru.arheo.core.db

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import ru.arheo.core.data.util.AppPaths

object DatabaseFactory {

    fun create(): Database {
        val dbPath = AppPaths.resolveDatabasePath()
        val database = Database.connect(
            url = "jdbc:sqlite:$dbPath",
            driver = "org.sqlite.JDBC",
        )
        transaction(database) {
            SchemaUtils.create(
                ReportTable,
                ReportAuthorTable,
                ReportDistrictTable,
                ReportKeywordTable,
                MonumentTable,
            )
        }
        return database
    }
}
