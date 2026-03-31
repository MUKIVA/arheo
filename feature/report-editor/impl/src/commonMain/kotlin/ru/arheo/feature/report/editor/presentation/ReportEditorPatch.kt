package ru.arheo.feature.report.editor.presentation

import ru.arheo.feature.report.editor.domian.models.report.Report
import ru.arheo.feature.report.editor.presentation.models.UiMonument

internal sealed interface ReportEditorPatch {
    data class ReportLoaded(val report: Report) : ReportEditorPatch
    data object ReportLoadError : ReportEditorPatch
//    data class SuggestionsLoaded(
//        val authors: List<Author>,
//        val workTypes: List<ReportWorkType>
//    ) : ReportEditorPatch
    data class NameChanged(val name: String) : ReportEditorPatch
    data class YearChanged(val year: String) : ReportEditorPatch
    data class AuthorsChanged(val authors: String) : ReportEditorPatch
    data class WorkTypeChanged(val workType: String) : ReportEditorPatch
    data class DistrictsChanged(val districts: String) : ReportEditorPatch
    data class KeywordsChanged(val keywords: String) : ReportEditorPatch
    data class MonumentUpdated(val index: Int, val monument: UiMonument) : ReportEditorPatch
    data object MonumentAdded : ReportEditorPatch
    data class MonumentRemoved(val index: Int) : ReportEditorPatch
//    data object Saving : ReportEditorPatch
//    data class Error(val message: String) : ReportEditorPatch
//    data object Saved : ReportEditorPatch
}