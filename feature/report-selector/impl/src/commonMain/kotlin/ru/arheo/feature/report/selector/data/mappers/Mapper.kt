package ru.arheo.feature.report.selector.data.mappers

import ru.arheo.core.domain.model.FileInfoData

internal object Mapper {
    fun from(data: FileInfoData): FileInfoDataMapper {
        return FileInfoDataMapper(data)
    }
}