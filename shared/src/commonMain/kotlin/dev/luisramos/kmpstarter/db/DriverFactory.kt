package dev.luisramos.kmpstarter

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import dev.luisramos.kmmstarter.db.Database
import dev.luisramos.kmpstarter.db.Todo
import kotlinx.datetime.LocalDateTime

interface DriverFactory {
    fun createDriver(): SqlDriver
}

object LocalDateTimeColumnAdapter : ColumnAdapter<LocalDateTime, String> {
    override fun decode(databaseValue: String): LocalDateTime =
        LocalDateTime.parse(databaseValue)

    override fun encode(value: LocalDateTime): String = value.toString()
}

fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()
    return Database(
        driver = driver,
        todoAdapter = Todo.Adapter(
            createdAtAdapter = LocalDateTimeColumnAdapter,
            updatedAtAdapter = LocalDateTimeColumnAdapter,
            doneAtAdapter = LocalDateTimeColumnAdapter
        )
    )
}