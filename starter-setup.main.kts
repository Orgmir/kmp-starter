#!/usr/bin/env kotlin

import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.copyTo
import kotlin.io.path.isDirectory
import kotlin.io.path.name
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

listOf(
    "shared/src/androidMain/kotlin/",
    "shared/src/commonMain/kotlin/",
    "shared/src/iosMain/kotlin/",
    "androidApp/src/main/java/",
).forEach { path ->
    // find all files in folder
    Files.walk(Path.of(path))
        .filter(Files::isRegularFile)
        .forEach { file ->
            // Rewrite package
            val text = file.readText()
            // get old package
            val oldPackage = "(?<=^package\\s)(.*)".toRegex().find(text)?.value.orEmpty()
            // replace with new package
            if (oldPackage.isNotEmpty()) {
                println("${file.pathString}: Renaming package")
                val newText = text.replace(oldPackage, packageName)
                FileWriter(file.toFile()).use { it.write(newText) }
            }

            // move file to new folder structure
            val newFolder = Path.of("$path$folders", file.name)
            if (!newFolder.isDirectory()) {
                newFolder.toFile().mkdirs()
            }
            println("${file.pathString}: Moving to ${newFolder.pathString}")
            file.copyTo(newFolder, overwrite = true)
        }
}


// Move folders in shared and android to match package name
//  shared/src/androidMain/kotlin
//  shared/src/commonMain/kotlin
//  shared/src/iosMain/kotlin
//  androidApp/src/main/java
// Rename package in all kotlin files
// Rename bundle from "orgIdentifier.iosApp" to app namespace in iosApp.xcodeproj/project.pbxproj


fun replaceInFile(filename: String, regex: Regex, newValue: String) {
    val file = File(filename)
    val newFile = file.readText()
        .replace(regex, newValue)
    FileWriter(file).use { it.write(newFile) }
}