package ru.arheo.core.util

import java.nio.file.Files
import java.nio.file.Path

object AppPaths {

    private const val META_DIRECTORY_NAME = "meta"
    private const val DATABASE_FILE_NAME = "arheo.db"
    private const val RESOURCES_DIRECTORY_NAME = "resources"
    private const val COMPOSE_RESOURCES_PROPERTY = "compose.application.resources.dir"
    private const val BUILD_PATH_MARKER = "build"

    fun resolveMetaDirectory(): Path {
        val baseDir = resolveBaseDirectory()
        val metaDir = baseDir.resolve(META_DIRECTORY_NAME)
        Files.createDirectories(metaDir)
        return metaDir
    }

    fun resolveDatabasePath(): String =
        resolveMetaDirectory().resolve(DATABASE_FILE_NAME).toString()

    private fun resolveBaseDirectory(): Path {
        val resourcesDir = System.getProperty(COMPOSE_RESOURCES_PROPERTY)
        if (resourcesDir != null) {
            val resourcesPath = Path.of(resourcesDir)
            val isDistribution = !resourcesPath.toString().contains(BUILD_PATH_MARKER)
            if (isDistribution) return resourcesPath
        }
        return Path.of(System.getProperty("user.dir"), RESOURCES_DIRECTORY_NAME)
    }
}
