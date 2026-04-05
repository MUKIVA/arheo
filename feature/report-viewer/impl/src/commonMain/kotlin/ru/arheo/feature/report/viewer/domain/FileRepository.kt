package ru.arheo.feature.report.viewer.domain

internal interface FileRepository {

    suspend fun extractArchiveAndOpenInExplorer(
        archiveFilePath: String?
    ): Boolean

}