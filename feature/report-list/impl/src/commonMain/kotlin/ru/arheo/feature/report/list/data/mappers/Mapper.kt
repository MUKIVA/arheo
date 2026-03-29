package ru.arheo.feature.report.list.data.mappers

import ru.arheo.core.domain.model.DataReport

internal object Mapper {
    fun from(data: DataReport): DataReportMapper {
        return DataReportMapper(data)
    }
}