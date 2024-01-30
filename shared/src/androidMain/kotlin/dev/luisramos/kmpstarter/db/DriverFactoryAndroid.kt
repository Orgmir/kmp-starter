package dev.luisramos.kmpstarter.db

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.luisramos.kmmstarter.db.Database
import dev.luisramos.kmpstarter.DriverFactory

class DriverFactoryAndroid(private val context: Context) : DriverFactory {
    override fun createDriver(): SqlDriver {
        val schema = Database.Schema
        return AndroidSqliteDriver(
            schema = schema,
            context = context,
            name = "database.db",
            // LR: Speeds up database
            // https://stackoverflow.com/questions/65425352/sqldelight-slow-performance-compared-to-room
            callback = object : AndroidSqliteDriver.Callback(schema) {
                override fun onConfigure(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    db.query("PRAGMA JOURNAL_MODE = WAL").use { it.moveToFirst() }
                    db.query("PRAGMA SYNCHRONOUS = 2").use { it.moveToFirst() }
                }
            }
        )
    }
}