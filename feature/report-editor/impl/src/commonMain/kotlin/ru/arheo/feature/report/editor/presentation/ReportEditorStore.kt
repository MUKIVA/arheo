package ru.arheo.feature.report.editor.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.mvikotlin.core.store.Store
import ru.arheo.core.domain.model.Monument
import ru.arheo.feature.report.editor.domian.models.report.ReportId
import ru.arheo.feature.report.editor.domian.models.report.ReportName
import ru.arheo.feature.report.editor.domian.models.report.ReportYear
import ru.arheo.feature.report.editor.presentation.models.UiMonument

internal class ReportEditorStore(
    implementation: Store<Intent, State, Label>
)
    : Store<ReportEditorStore.Intent, ReportEditorStore.State, ReportEditorStore.Label>
    by implementation
{

    sealed interface Intent {
        data class UpdateName(val name: String) : Intent
        data class UpdateYear(val year: String) : Intent
        data class UpdateAuthors(val authors: String) : Intent
        data class UpdateWorkType(val workType: String) : Intent
        data class UpdateDistricts(val districts: String) : Intent
        data class UpdateKeywords(val keywords: String) : Intent
        data class UpdateMonument(val index: Int, val monument: UiMonument) : Intent
        data object AddMonument : Intent
        data class RemoveMonument(val index: Int) : Intent
        data class Save(val workingDirectory: String, val hasFiles: Boolean) : Intent
    }

    @Stable
    sealed interface State {

        @Immutable
        data object Error : State

        @Immutable
        data object Loading : State

        @Immutable
        data class Content(
            val reportId: Long? = null,
            val name: String = String(),
            val year: String = String(),
            val authors: String = String(),
            val workType: String = String(),
            val districts: String = String(),
            val keywords: String = String(),
            val monuments: List<UiMonument> = emptyList(),
            val isSaving: Boolean = false,
            val error: String? = null,
//            val isLoading: Boolean = false,
//            val authorSuggestions: List<String> = emptyList(),
//            val workTypeSuggestions: List<String> = emptyList(),
        ) : State {
            val isEditing: Boolean get() = reportId != null
        }
    }



    sealed interface Label {
        data object Saved : Label
        data class ArchivePathLoaded(val archivePath: String?) : Label
    }
}
