package ru.arheo.feature.report.editor.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import arheo.feature.report_editor.impl.generated.resources.Res
import arheo.feature.report_editor.impl.generated.resources.editor_chip_action_add
import org.jetbrains.compose.resources.stringResource
import ru.arheo.feature.report.editor.ui.CloseIcon

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ChipInputField(
    label: String,
    placeholder: String,
    emptyMessage: String,
    chips: List<String>,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier,
    inputText: MutableState<String> = remember { mutableStateOf(String()) }
) = Column(modifier) {
    ChipList(
        chips = chips,
        onRemove = onRemove,
        emptyMessage = emptyMessage,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.large
            )
            .padding(16.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    ChipInput(inputText, label, placeholder, Modifier.fillMaxWidth()) {
        onDone(inputText, chips, onAdd)
    }
}

@Composable
private fun ChipInput(
    inputText: MutableState<String>,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    onDone: () -> Unit = {},
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    OutlinedTextField(
        value = inputText.value,
        shape = MaterialTheme.shapes.large,
        onValueChange = { inputText.value = it },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onDone() } ),
        modifier = Modifier.weight(1f)
    )
    OutlinedButton(
        onClick = onDone,
        shape = MaterialTheme.shapes.large,
        enabled = inputText.value.isNotBlank()
    ) {
        Text(stringResource(Res.string.editor_chip_action_add))
    }
}

@Composable
private fun ChipList(
    chips: List<String>,
    onRemove: (String) -> Unit,
    emptyMessage: String,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier
) {
    when {
        chips.isNotEmpty() -> FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            chips.forEach { chip ->
                InputChip(
                    selected = false,
                    onClick = { onRemove(chip) },
                    label = { Text(chip) },
                    trailingIcon = {
                        Icon(
                            imageVector = CloseIcon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                    },
                )
            }
        }
        else -> Text(
            text = emptyMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun onDone(
    inputText: MutableState<String>,
    chips: List<String>,
    onAdd: (String) -> Unit
) {
    commitChip(inputText.value, chips) { value ->
        onAdd(value)
        inputText.value = String()
    }
}

private fun commitChip(
    text: String,
    chips: List<String>,
    onAdd: (String) -> Unit
) {
    val trimmed = text.trim()
    if (trimmed.isNotEmpty() && trimmed !in chips) {
        onAdd(trimmed)
    }
}