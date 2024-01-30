data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val build: Int
) {
    val name get() = "$major.$minor.$patch"
    val fullName get() = "$name ($build)"
    val code get() = major * 1000000 + minor * 10000 + patch * 100 + build
}

const val AppNamespace = "dev.luisramos.kmpstarter"

object Versions {
    private const val BuildNumber = 1
    val Build get() = System.getProperty("buildNumber")?.toInt() ?: BuildNumber

    val App = Version(0, 1, 0, Build)

    object Android {
        const val CompileSdk = 34
        const val MinSdk = 24
        const val TargetSdk = 34
    }

    const val Kotlin = "1.9.21"
    const val Ktor = "2.3.7"
    const val SqlDelight = "2.0.1"
    const val ComposeCompiler = "1.5.8"
}

object Dependencies {
    const val Datetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.5.0"
    const val Serialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2"
    const val Settings = "com.russhwolf:multiplatform-settings-no-arg:1.1.1"
    const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3"

    object Kermit {
        const val version = "2.0.2"
        const val Core = "co.touchlab:kermit:$version"
    }

    object Ktor {
        const val Core = "io.ktor:ktor-client-core:${Versions.Ktor}"
        const val OkHttp = "io.ktor:ktor-client-okhttp:${Versions.Ktor}"
        const val iOS = "io.ktor:ktor-client-ios:${Versions.Ktor}"
        const val Serialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.Ktor}"
    }

    object SqlDelight {
        const val Android = "app.cash.sqldelight:android-driver:${Versions.SqlDelight}"
        const val iOS = "app.cash.sqldelight:native-driver:${Versions.SqlDelight}"
        const val Jdbc = "app.cash.sqldelight:sqlite-driver:${Versions.SqlDelight}"
        const val Coroutines =
            "app.cash.sqldelight:coroutines-extensions:${Versions.SqlDelight}"
    }

    object Android {

        const val Lifecycle = "androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0"
        const val LifecycleCompose = "androidx.lifecycle:lifecycle-runtime-compose:2.7.0"

        object Compose {
            const val Version = "1.6.0"

            const val Ui = "androidx.compose.ui:ui:$Version"
            const val UiTooling = "androidx.compose.ui:ui-tooling:$Version"
            const val UiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$Version"
            const val Foundation = "androidx.compose.foundation:foundation:1.3.1"
            const val Material = "androidx.compose.material:material:1.3.1"
            const val Activity = "androidx.activity:activity-compose:1.6.1"
        }
    }
}