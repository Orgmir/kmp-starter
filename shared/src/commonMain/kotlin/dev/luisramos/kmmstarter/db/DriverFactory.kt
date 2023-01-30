package dev.luisramos.kmmstarter.db

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): KmmDatabase {
    val driver = driverFactory.createDriver()
    val database = KmmDatabase(driver)

    val playerQueries: HockeyPlayerQueries = database.hockeyPlayerQueries

    playerQueries.insert(player_number = 10, full_name = "Corey Perry")

    val player = HockeyPlayer(10, "Ronald McDonald")
    playerQueries.insertFullPlayerObject(player)

    return database
}