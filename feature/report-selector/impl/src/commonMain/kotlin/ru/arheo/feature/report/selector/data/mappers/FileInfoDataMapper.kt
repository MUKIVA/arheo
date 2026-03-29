package ru.arheo.feature.report.selector.data.mappers

import ru.arheo.core.domain.model.FileInfoData
import ru.arheo.feature.report.selector.domain.models.FileInfo
import ru.arheo.feature.report.selector.domain.models.FileInfoAbsolutePath
import ru.arheo.feature.report.selector.domain.models.FileInfoIsDirectory
import ru.arheo.feature.report.selector.domain.models.FileInfoName
import ru.arheo.feature.report.selector.domain.models.FileInfoSize

@JvmInline
internal value class FileInfoDataMapper(
    private val data: FileInfoData
) {
    fun toDomain(): FileInfo {
        return FileInfo(
            name = FileInfoName(data.name) ,
            absolutePath = FileInfoAbsolutePath(data.absolutePath),
            size = FileInfoSize(data.size),
            isDirectory = FileInfoIsDirectory(data.isDirectory)
        )
    }
}