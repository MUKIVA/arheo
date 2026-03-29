package ru.arheo.core.data.util

import java.nio.file.Files
import java.nio.file.Path

object AppPaths {

    private const val META_DIRECTORY_NAME = "meta"
    private const val ARCHIVES_DIRECTORY_NAME = "archives"
    private const val TMP_DIRECTORY_NAME = "tmp"
    private const val SCRIPTS_DIRECTORY_NAME = "scripts"
    private const val DATABASE_FILE_NAME = "arheo.db"
    private const val RESOURCES_DIRECTORY_NAME = "resources"
    private const val COMPOSE_RESOURCES_PROPERTY = "compose.application.resources.dir"
    private const val BUILD_PATH_MARKER = "build"

    fun resolveMetaDirectory(): Path =
        resolveAndCreate(resolveDataDirectory().resolve(META_DIRECTORY_NAME))

    fun resolveDatabasePath(): String =
        resolveMetaDirectory().resolve(DATABASE_FILE_NAME).toString()

    fun resolveArchivesDirectory(): Path =
        resolveAndCreate(resolveMetaDirectory().resolve(ARCHIVES_DIRECTORY_NAME))

    fun resolveTmpDirectory(): Path =
        resolveAndCreate(resolveDataDirectory().resolve(TMP_DIRECTORY_NAME))

    fun resolveScriptsDirectory(): Path {
        val resourcesDir = System.getProperty(COMPOSE_RESOURCES_PROPERTY)
        if (resourcesDir != null) {
            return Path.of(resourcesDir).resolve(SCRIPTS_DIRECTORY_NAME)
        }
        return resolveDataDirectory().resolve(SCRIPTS_DIRECTORY_NAME)
    }

    private fun resolveAndCreate(path: Path): Path {
        Files.createDirectories(path)
        return path
    }

    private fun resolveDataDirectory(): Path {
        val resourcesDir = System.getProperty(COMPOSE_RESOURCES_PROPERTY)
        if (resourcesDir != null) {
            val resourcesPath = Path.of(resourcesDir)
            val isDistribution = !resourcesPath.toString().contains(BUILD_PATH_MARKER)
            if (isDistribution) return resourcesPath
        }
        return Path.of(System.getProperty("user.dir"), RESOURCES_DIRECTORY_NAME)
    }
}
