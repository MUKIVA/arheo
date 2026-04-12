package ru.arheo.feature.report.editor.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.arkivanov.mvikotlin.core.store.Store
import ru.arheo.feature.report.editor.presentation.models.SaveValidationError
import ru.arheo.feature.report.editor.presentation.models.UiMonument
import java.nio.file.Path

internal class ReportEditorStore(
    implementation: Store<Intent, State, Label>
)
    : Store<ReportEditorStore.Intent, ReportEditorStore.State, ReportEditorStore.Label>
    by implementation
{

    sealed interface Intent {
        data class UpdateName(val name: String) : Intent
        data class UpdateYear(val year: String) : Intent
        data class UpdateWorkType(val workType: String) : Intent
        data class AddAuthor(val author: String) : Intent
        data class RemoveAuthor(val author: String) : Intent
        data class AddDistrict(val district: String) : Intent
        data class RemoveDistrict(val district: String) : Intent
        data class AddKeyword(val keyword: String) : Intent
        data class RemoveKeyword(val keyword: String) : Intent
        data class UpdateMonument(val index: Int, val monument: UiMonument) : Intent
        data object AddMonument : Intent
        data class RemoveMonument(val index: Int) : Intent
        data object Save : Intent
    }

    @Stable
    sealed interface State {

        @Immutable
        data object Error : State

        @Immutable
        data object Loading : State

        @Immutable
        data class Content(
            val woking: Path,
            val reportId: Long? = null,
            val name: String = String(),
            val year: String = String(),
            val authors: List<String> = emptyList(),
            val workType: String = String(),
            val districts: List<String> = emptyList(),
            val keywords: List<String> = emptyList(),
            val monuments: List<UiMonument> = emptyList(),
            val archive: Path? = null,
            val isSaving: Boolean = false,
        ) : State {
            val isEditing: Boolean get() = reportId != null
        }
    }

    sealed interface Label {
        data object Saved : Label
        data class SaveError(val error: SaveValidationError) : Label
    }
}
