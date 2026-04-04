package ru.arheo.feature.report.editor.presentation.models

internal enum class SaveValidationError {
    EMPTY_TITLE,
    INVALID_YEAR,
    NO_AUTHORS,
    SAVE_FAILED,
}
