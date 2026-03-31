package ru.arheo.feature.report.editor.presentation

import ru.arheo.feature.report.editor.domian.models.report.Report


internal sealed interface ReportEditorAction {
//    data class ReportLoaded(
//        val report: Report
//    ) : ReportEditorAction

//    data class SuggestionsLoaded(
//        val authors: List<Author>,
//        val workTypes: List<ReportWorkType>
//    ) : ReportEditorAction

    data class ReportLoaded(
        val report: Report
    ) : ReportEditorAction

    data object ReportLoadError : ReportEditorAction
}