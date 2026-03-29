package ru.arheo.feature.report.list.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import kotlinx.coroutines.launch
import ru.arheo.feature.report.list.domain.ReportRepository

internal class ReportListBootstrapper(
    private val repository: ReportRepository
) : CoroutineBootstrapper<ReportListAction>() {
    override fun invoke() {
        scope.launch {
            val reports = repository.getAllReports()
            dispatch(ReportListAction.ReportsLoaded(reports))
        }
    }
}