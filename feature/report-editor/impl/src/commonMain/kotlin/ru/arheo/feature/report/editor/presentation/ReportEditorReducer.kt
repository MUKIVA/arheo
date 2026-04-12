package ru.arheo.feature.report.editor.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import ru.arheo.feature.report.editor.domian.models.monument.Monument
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
        is ReportEditorPatch.ReportLoaded -> ReportEditorStore.State.Content(
            reportId = patch.report.id?.value,
            name = patch.report.name.value,
            year = patch.report.year.value.let { if (it == 0) "" else it.toString() },
            authors = patch.report.authors.map { it.value },
            workType = patch.report.workType.value,
            districts = patch.report.districts.map { it.value },
            keywords = patch.report.keywords.map { it.value },
            monuments = patch.report.monuments.map { it.toUi() },
            archive = patch.report.archive,
            woking = patch.working
        )
        is ReportEditorPatch.ReportLoadError -> ReportEditorStore.State.Error
        else -> state
    }

    private fun reduce(
        state: ReportEditorStore.State.Content,
        patch: ReportEditorPatch
    ) = when (patch) {
        is ReportEditorPatch.NameChanged -> state.copy(name = patch.name)
        is ReportEditorPatch.YearChanged -> state.copy(year = patch.year)
        is ReportEditorPatch.WorkTypeChanged -> state.copy(workType = patch.workType)
        is ReportEditorPatch.AuthorAdded -> state.copy(
            authors = state.authors + patch.author.trim()
        )
        is ReportEditorPatch.AuthorRemoved -> state.copy(
            authors = state.authors - patch.author
        )
        is ReportEditorPatch.DistrictAdded -> state.copy(
            districts = state.districts + patch.district.trim()
        )
        is ReportEditorPatch.DistrictRemoved -> state.copy(
            districts = state.districts - patch.district
        )
        is ReportEditorPatch.KeywordAdded -> state.copy(
            keywords = state.keywords + patch.keyword.trim()
        )
        is ReportEditorPatch.KeywordRemoved -> state.copy(
            keywords = state.keywords - patch.keyword
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
        is ReportEditorPatch.Saving -> state.copy(isSaving = true)
        is ReportEditorPatch.SaveFinished -> state.copy(isSaving = false)
        else -> state
    }

    private fun reduce(
        state: ReportEditorStore.State.Error,
        patch: ReportEditorPatch
    ) = when (patch) {
        else -> state
    }

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
