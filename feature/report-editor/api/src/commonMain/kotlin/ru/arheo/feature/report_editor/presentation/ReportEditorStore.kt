package ru.arheo.feature.report_editor.presentation

import com.arkivanov.mvikotlin.core.store.Store
import ru.arheo.core.domain.model.Monument

interface ReportEditorStore : Store<ReportEditorStore.Intent, ReportEditorStore.State, ReportEditorStore.Label> {

    sealed interface Intent {
        data class UpdateTitle(val title: String) : Intent
        data class UpdateYear(val year: String) : Intent
        data class UpdateAuthors(val authors: String) : Intent
        data class UpdateWorkType(val workType: String) : Intent
        data class UpdateDistricts(val districts: String) : Intent
        data class UpdateKeywords(val keywords: String) : Intent
        data class UpdateMonument(val index: Int, val monument: Monument) : Intent
        data object AddMonument : Intent
        data class RemoveMonument(val index: Int) : Intent
        data object Save : Intent
    }

    data class State(
        val reportId: Long? = null,
        val title: String = "",
        val year: String = "",
        val authors: String = "",
        val workType: String = "",
        val districts: String = "",
        val keywords: String = "",
        val monuments: List<Monument> = emptyList(),
        val isSaving: Boolean = false,
        val error: String? = null,
        val isLoading: Boolean = false,
        val authorSuggestions: List<String> = emptyList(),
        val workTypeSuggestions: List<String> = emptyList(),
    ) {
        val isEditing: Boolean get() = reportId != null
    }

    sealed interface Label {
        data object Saved : Label
    }
}
