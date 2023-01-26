package dev.luisramos.mppstarter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform