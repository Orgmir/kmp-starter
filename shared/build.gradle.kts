plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("app.cash.sqldelight")
}

kotlin {
    android()

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


                with(Dependencies.Ktor) {
                    implementation(Core)
                    implementation(Serialization)
                }
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

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(Dependencies.Ktor.iOS)
                implementation(Dependencies.SqlDelight.iOS)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
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
        create("KmmDatabase") {
            packageName.set("dev.luisramos.kmmstarter.db")
        }
    }
}
