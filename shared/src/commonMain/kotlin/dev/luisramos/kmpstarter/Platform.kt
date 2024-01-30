package dev.luisramos.kmpstarter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform