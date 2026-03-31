package ru.arheo.feature.report.editor.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import ru.arheo.feature.report.editor.domian.models.monument.Monument
import ru.arheo.feature.report.editor.domian.models.report.Report
import ru.arheo.feature.report.editor.presentation.models.UiMonument

internal class ReportEditorReducer : Reducer<ReportEditorStore.State, ReportEditorPatch> {
    override fun ReportEditorStore.State.reduce(
        msg: ReportEditorPatch
    ): ReportEditorStore.State = when (this) {
        is ReportEditorStore.State.Loading -> reduce(this, msg)
        is ReportEditorStore.State.Content -> reduce(this, msg)
        is ReportEditorStore.State.Error ->   reduce(this, msg)
    }

    private fun reduce(
        state: ReportEditorStore.State.Loading,
        patch: ReportEditorPatch
    ) = when (patch) {
        is ReportEditorPatch.ReportLoaded -> patch.report.toContentState()
        is ReportEditorPatch.ReportLoadError -> ReportEditorStore.State.Error
        else -> state
    }

    private fun reduce(
        state: ReportEditorStore.State.Content,
        patch: ReportEditorPatch
    ) = when (patch) {
        is ReportEditorPatch.NameChanged -> state.copy(
            name = patch.name, error = null
        )
        is ReportEditorPatch.YearChanged -> state.copy(
            year = patch.year, error = null
        )
        is ReportEditorPatch.WorkTypeChanged -> state.copy(
            workType = patch.workType, error = null
        )
        is ReportEditorPatch.AuthorAdded -> state.copy(
            authors = state.authors + patch.author.trim(), error = null
        )
        is ReportEditorPatch.AuthorRemoved -> state.copy(
            authors = state.authors - patch.author, error = null
        )
        is ReportEditorPatch.DistrictAdded -> state.copy(
            districts = state.districts + patch.district.trim(), error = null
        )
        is ReportEditorPatch.DistrictRemoved -> state.copy(
            districts = state.districts - patch.district, error = null
        )
        is ReportEditorPatch.KeywordAdded -> state.copy(
            keywords = state.keywords + patch.keyword.trim(), error = null
        )
        is ReportEditorPatch.KeywordRemoved -> state.copy(
            keywords = state.keywords - patch.keyword, error = null
        )
        is ReportEditorPatch.MonumentUpdated -> state.copy(
            monuments = state.monuments.toMutableList().apply {
                set(patch.index, patch.monument)
            }
        )
        is ReportEditorPatch.MonumentRemoved -> state.copy(
            monuments = state.monuments.toMutableList().apply {
                removeAt(patch.index)
            }
        )
        is ReportEditorPatch.MonumentAdded -> state.copy(
            monuments = state.monuments + UiMonument.default()
        )
        is ReportEditorPatch.Saving -> state.copy(
            isSaving = true, error = null
        )
        is ReportEditorPatch.SaveError -> state.copy(
            isSaving = false, error = patch.message
        )
        is ReportEditorPatch.Saved -> state.copy(
            isSaving = false
        )
        else -> state
    }

    private fun reduce(
        state: ReportEditorStore.State.Error,
        patch: ReportEditorPatch
    ) = when (patch) {
        else -> state
    }

    private fun Report.toContentState(): ReportEditorStore.State.Content =
        ReportEditorStore.State.Content(
            reportId = id?.value,
            name = name.value,
            year = year.value.let { if (it == 0) "" else it.toString() },
            authors = authors.map { it.value },
            workType = workType.value,
            districts = districts.map { it.value },
            keywords = keywords.map { it.value },
            monuments = monuments.map { it.toUi() },
            archiveFilePath = archiveFilePath,
        )

    private fun Monument.toUi(): UiMonument = UiMonument(
        id = id,
        name = name,
        type = type,
        culture = culture,
        period = period,
        geographicLocation = geographicLocation,
        number = number,
    )
}
