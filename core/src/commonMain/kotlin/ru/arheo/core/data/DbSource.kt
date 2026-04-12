package ru.arheo.core.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

internal abstract class DbSource(
    private val database: Database
) {

    protected suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) { transaction(database) { block() } }


}