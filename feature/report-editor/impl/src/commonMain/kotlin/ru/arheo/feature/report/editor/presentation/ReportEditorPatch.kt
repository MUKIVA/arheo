package ru.arheo.feature.report.editor.presentation

import ru.arheo.feature.report.editor.domian.models.report.Report
import ru.arheo.feature.report.editor.presentation.models.UiMonument

internal sealed interface ReportEditorPatch {
    data class ReportLoaded(val report: Report) : ReportEditorPatch
    data object ReportLoadError : ReportEditorPatch
    data class NameChanged(val name: String) : ReportEditorPatch
    data class YearChanged(val year: String) : ReportEditorPatch
    data class WorkTypeChanged(val workType: String) : ReportEditorPatch
    data class AuthorAdded(val author: String) : ReportEditorPatch
    data class AuthorRemoved(val author: String) : ReportEditorPatch
    data class DistrictAdded(val district: String) : ReportEditorPatch
    data class DistrictRemoved(val district: String) : ReportEditorPatch
    data class KeywordAdded(val keyword: String) : ReportEditorPatch
    data class KeywordRemoved(val keyword: String) : ReportEditorPatch
    data class MonumentUpdated(val index: Int, val monument: UiMonument) : ReportEditorPatch
    data object MonumentAdded : ReportEditorPatch
    data class MonumentRemoved(val index: Int) : ReportEditorPatch
    data object Saving : ReportEditorPatch
    data object SaveFinished : ReportEditorPatch
}
