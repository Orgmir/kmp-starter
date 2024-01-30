plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("app.cash.sqldelight")
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependencies.Coroutines)
                implementation(Dependencies.Datetime)
                implementation(Dependencies.Settings)
                implementation(Dependencies.SqlDelight.Coroutines)
                implementation(Dependencies.Ktor.Core)
                implementation(Dependencies.Ktor.Serialization)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.OkHttp)
                implementation(Dependencies.SqlDelight.Android)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(Dependencies.SqlDelight.Jdbc)
            }
        }

        val iosMain by creating {
            dependencies {
                implementation(Dependencies.Ktor.iOS)
                implementation(Dependencies.SqlDelight.iOS)
            }
        }
    }
}

android {
    namespace = AppNamespace
    compileSdk = Versions.Android.CompileSdk
    defaultConfig {
        minSdk = Versions.Android.MinSdk
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("dev.luisramos.kmpstarter.db")
        }
    }
}
