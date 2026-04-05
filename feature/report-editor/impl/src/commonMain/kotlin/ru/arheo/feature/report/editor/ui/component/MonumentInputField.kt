package ru.arheo.feature.report.editor.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import arheo.feature.report_editor.impl.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import ru.arheo.feature.report.editor.domian.models.monument.MonumentCulture
import ru.arheo.feature.report.editor.domian.models.monument.MonumentLocation
import ru.arheo.feature.report.editor.domian.models.monument.MonumentName
import ru.arheo.feature.report.editor.domian.models.monument.MonumentNumber
import ru.arheo.feature.report.editor.domian.models.monument.MonumentPeriod
import ru.arheo.feature.report.editor.domian.models.monument.MonumentType
import ru.arheo.feature.report.editor.presentation.models.UiMonument

@Composable
internal fun MonumentInputField(
    monument: UiMonument,
    onUpdate: (UiMonument) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier
) {
    MonumentNameInput(
        monument = monument,
        onUpdate = onUpdate,
        modifier = Modifier
            .padding(16.dp)
    )
    HorizontalDivider(Modifier.fillMaxWidth().padding(top = 8.dp))
    MonumentInfoInput(
        monument = monument,
        onUpdate = onUpdate,
        modifier = Modifier
            .padding(16.dp)
    )
    HorizontalDivider(Modifier.fillMaxWidth().padding(top = 8.dp))
    MonumentActions(
        onRemove = onRemove,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
private fun MonumentActions(
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) = FlowRow(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    TextButton(
        onClick = onRemove,
        shape = MaterialTheme.shapes.large,
    ) {
        Text(
            text = stringResource(Res.string.editor_monument_action_remove),
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
private inline fun MonumentInfoInput(
    monument: UiMonument,
    crossinline onUpdate: (UiMonument) -> Unit,
    modifier: Modifier = Modifier
) = Column(modifier) {
    OutlinedTextField(
        value = monument.type.value,
        shape = MaterialTheme.shapes.large,
        onValueChange = { onUpdate(monument.copy(type = MonumentType(it))) },
        label = { Text(stringResource(Res.string.editor_monument_field_type)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    OutlinedTextField(
        value = monument.culture.value,
        shape = MaterialTheme.shapes.large,
        onValueChange = { onUpdate(monument.copy(culture = MonumentCulture(it))) },
        label = { Text(stringResource(Res.string.editor_monument_field_culture)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    OutlinedTextField(
        value = monument.period.value,
        shape = MaterialTheme.shapes.large,
        onValueChange = { onUpdate(monument.copy(period = MonumentPeriod(it))) },
        label = { Text(stringResource(Res.string.editor_monument_field_period)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
    OutlinedTextField(
        value = monument.geographicLocation.value,
        shape = MaterialTheme.shapes.large,
        onValueChange = { onUpdate(monument.copy(geographicLocation = MonumentLocation(it))) },
        label = { Text(stringResource(Res.string.editor_monument_field_location)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private inline fun MonumentNameInput(
    monument: UiMonument,
    crossinline onUpdate: (UiMonument) -> Unit,
    modifier: Modifier = Modifier
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    OutlinedTextField(
        value = monument.name.value,
        shape = MaterialTheme.shapes.large,
        onValueChange = { onUpdate(monument.copy(name = MonumentName(it))) },
        label = { Text(stringResource(Res.string.editor_monument_field_name)) },
        singleLine = true,
        modifier = Modifier.weight(0.5f),
    )
    OutlinedTextField(
        value = monument.number.value,
        shape = MaterialTheme.shapes.large,
        onValueChange = { onUpdate(monument.copy(number = MonumentNumber(it))) },
        label = { Text(stringResource(Res.string.editor_monument_field_number)) },
        singleLine = true,
        modifier = Modifier.weight(0.5f),
    )
}