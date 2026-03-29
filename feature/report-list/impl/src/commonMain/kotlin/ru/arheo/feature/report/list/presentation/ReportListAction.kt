package ru.arheo.feature.report.list.presentation

import ru.arheo.feature.report.list.domain.models.Report

internal sealed interface ReportListAction {
    data class ReportsLoaded(val reports: List<Report>) : ReportListAction
}