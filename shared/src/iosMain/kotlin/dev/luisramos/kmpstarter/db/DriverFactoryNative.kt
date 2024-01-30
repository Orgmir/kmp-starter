package dev.luisramos.kmpstarter.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import dev.luisramos.kmpstarter.DriverFactory

class DriverFactoryNative : DriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(Database.Schema, "database.db")
    }
}