package ru.arheo.feature.report.editor.presentation

import ru.arheo.feature.report.editor.domian.models.report.Report
import java.nio.file.Path


internal sealed interface ReportEditorAction {
    data class ReportLoaded(
        val report: Report,
        val working: Path
    ) : ReportEditorAction

    data object ReportLoadError : ReportEditorAction
}