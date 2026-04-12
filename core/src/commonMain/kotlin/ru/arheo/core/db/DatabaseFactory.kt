package ru.arheo.core.db

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists

object DatabaseFactory {
    private const val DATABASE_DIRECTORY = "data"
    private const val DATABASE_FILE_NAME = "$DATABASE_DIRECTORY/arheo.db"
    private const val DATABASE_URL = "jdbc:sqlite:$DATABASE_FILE_NAME"
    private const val DATABASE_DRIVER = "org.sqlite.JDBC"

    init {
        val dbDirectory = Path.of(DATABASE_DIRECTORY)
        if (!dbDirectory.exists()) {
            Files.createDirectories(dbDirectory)
        }
    }

    fun create(): Database {
        val database = Database.connect(
            url = DATABASE_URL,
            driver = DATABASE_DRIVER,
        )

        initSchema(database)

        return database
    }

    private fun initSchema(database: Database) {
        transaction(database) {
            SchemaUtils.create(
                ReportTable,
                ReportAuthorTable,
                ReportDistrictTable,
                ReportKeywordTable,
                MonumentTable,
            )
        }
    }
}
