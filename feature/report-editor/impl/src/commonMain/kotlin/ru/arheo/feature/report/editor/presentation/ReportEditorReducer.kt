package ru.arheo.feature.report.editor.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import ru.arheo.feature.report.editor.domian.models.monument.Monument
import ru.arheo.feature.report.editor.presentation.models.UiMonument

internal class ReportEditorReducer : Reducer<ReportEditorStore.State, ReportEditorPatch> {
    override fun ReportEditorStore.State.reduce(
        msg: ReportEditorPatch
    ): ReportEditorStore.State = when (this) {
//            is ReportEditorPatch.ReportLoaded -> copy(
//                name = msg.report.name.value,
//                year = msg.report.year.value.toString(),
//                authors = msg.report.authors.joinToString(", "),
//                workType = msg.report.workType.value,
//                districts = msg.report.districts.joinToString(", "),
//                keywords = msg.report.keywords.joinToString(", "),
//                monuments = msg.report.monuments.asUiMonuments(),
//                isLoading = false,
//            )
//            is ReportEditorPatch.SuggestionsLoaded -> copy(
//                authorSuggestions = msg.authors.map { it.value },
//                workTypeSuggestions = msg.workTypes.map { it.value },
//            )
//            is ReportEditorPatch.TitleChanged -> copy(name = msg.title, error = null)
//            is ReportEditorPatch.YearChanged -> copy(year = msg.year, error = null)
//            is ReportEditorPatch.AuthorsChanged -> copy(authors = msg.authors, error = null)
//            is ReportEditorPatch.WorkTypeChanged -> copy(workType = msg.workType, error = null)
//            is ReportEditorPatch.DistrictsChanged -> copy(districts = msg.districts, error = null)
//            is ReportEditorPatch.KeywordsChanged -> copy(keywords = msg.keywords, error = null)
//            is ReportEditorPatch.MonumentUpdated -> copy(
//                ,
//            )
//            is ReportEditorPatch.MonumentAdded -> copy(monuments = monuments + UiMonument())
//            is ReportEditorPatch.MonumentRemoved -> copy(
//                monuments = monuments.toMutableList().apply { removeAt(msg.index) },
//            )
//            is ReportEditorPatch.Saving -> copy(isSaving = true, error = null)
//            is ReportEditorPatch.Error -> copy(error = msg.message, isSaving = false)
//            is ReportEditorPatch.Saved -> copy(isSaving = false)
        is ReportEditorStore.State.Loading -> reduce(this, msg)
        is ReportEditorStore.State.Content -> reduce(this, msg)
        is ReportEditorStore.State.Error ->   reduce(this, msg)
    }

    private fun reduce(
        state: ReportEditorStore.State.Loading,
        patch: ReportEditorPatch
    ) = when (patch) {
        is ReportEditorPatch.ReportLoaded -> {
            ReportEditorStore.State.Content(
                reportId = patch.report.id?.value,
                name = patch.report.name.value
            )
        }
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
        is ReportEditorPatch.AuthorsChanged -> state.copy(
            authors = patch.authors, error = null
        )
        is ReportEditorPatch.WorkTypeChanged -> state.copy(
            workType = patch.workType, error = null
        )
        is ReportEditorPatch.DistrictsChanged -> state.copy(
            districts = patch.districts, error = null
        )
        is ReportEditorPatch.KeywordsChanged -> state.copy(
            keywords = patch.keywords, error = null
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
        else -> state
    }

    private fun reduce(
        state: ReportEditorStore.State.Error,
        patch: ReportEditorPatch
    ) = when (patch) {
        else -> state
    }

    private fun List<Monument>.asUiMonuments(): List<UiMonument> {
        return map { monument -> monument.asUiMonument() }
    }

    private fun Monument.asUiMonument(): UiMonument {
        return UiMonument(
            id = id,
            name = name,
            type = type,
            culture = culture,
            period = period,
            geographicLocation = geographicLocation,
            number = number
        )
    }
}

