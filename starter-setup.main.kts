#!/usr/bin/env kotlin

import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString
import kotlin.io.path.readText
import kotlin.system.exitProcess

println("Setting up starter project...")
println("Root project name:")
val projectName = readLine().orEmpty()
if (projectName.isBlank()) {
    println("Please provide a project name.")
    exitProcess(1)
}

println("App package/bundle id (com.example.app):")
val packageName = readLine().orEmpty()
if (packageName.isBlank()) {
    println("Please provide a valid package.")
    exitProcess(1)
}
val folders = packageName.replace(".", "/")

println("Setting rootProject.name in settings.gradle.kts to $projectName")
replaceInFile(
    "settings.gradle.kts",
    Regex("(?<=rootProject.name = \")(.*)(?=\")"), projectName
)

println("Setting AppNamespace in Dependencies.kt to $packageName")
replaceInFile(
    "buildSrc/src/main/kotlin/Dependencies.kt",
    Regex("(?<=const val AppNamespace = \")(.*)(?=\")"), packageName
)

println("Change database package in shared/build.gradle.kts")
replaceInFile(
    "shared/build.gradle.kts",
    Regex("(?<=packageName.set(\")(.*)(?=\"))"), packageName
)

println("Setting Bundle identifier in iOS .xcodeproj to $packageName")
replaceInFile(
    "iosApp/iosApp.xcodeproj/project.pbxproj",
    "(?<=PRODUCT_BUNDLE_IDENTIFIER = )(.*)(?=;)".toRegex(),
    packageName
)

listOf(
    "shared/src/androidMain/kotlin/",
    "shared/src/commonMain/kotlin/",
    "shared/src/commonMain/sqldelight/",
    "shared/src/iosMain/kotlin/",
    "androidApp/src/main/java/",
).forEach { pathname ->
    // find all files in folder
    val path = Path.of(pathname)
    Files.walk(path)
        .filter(Files::isRegularFile)
        .forEach { file ->

            // Start with shared/src/iosMain/kotlin/dev/luisramos/kmmstarter
            val rootPathCount = "$pathname$folders".split("/").size
            // db/DriverFactoryNative.kt
            val fileName = file.pathString.split("/").drop(rootPathCount).joinToString("/")
            // Replace existing path with new path based of
            val newFile = Path.of("$pathname$folders", fileName).toFile()

            // create folder structure
            newFile.parentFile.mkdirs()
            // move file to new folder structure
            newFile.createNewFile()

            val text = file.readText()
            // get old package
            val oldPackage = "(?<=^package\\s)(.*)".toRegex().find(text)?.value.orEmpty()
            // replace with new package
            if (oldPackage.isNotEmpty()) {
                val newPackage = packageName
                val newText = text.replace(oldPackage, newPackage)
                println("${file.pathString}: Renaming package, moving to ${newFile.absolutePath}")
                newFile.appendText(newText)
            } else {
                println("${file.pathString}: Moving to ${newFile.absolutePath}")
                newFile.appendText(text)
            }
            // delete existing file
            file.deleteIfExists()
        }

    // Delete empty directories
    deleteIfEmpty(path)
}

fun replaceInFile(filename: String, regex: Regex, newValue: String) {
    val file = File(filename)
    val newFile = file.readText()
        .replace(regex, newValue)
    FileWriter(file).use { it.write(newFile) }
}

fun deleteIfEmpty(dir: Path) {
    // ignore if file
    if (!dir.isDirectory()) return
    // Try to delete children
    Files.list(dir).forEach {
        deleteIfEmpty(it)
    }
    // delete self if empty
    if (!Files.list(dir).findAny().isPresent) {
        Files.delete(dir)
    }
}