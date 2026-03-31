package ru.arheo.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class ReportList(
            val componentContext: ComponentContext,
            val navigateCreateReport: () -> Unit,
            val navigateEditReport: (Long) -> Unit
        ) : Child()
        data class ReportEditor(
            val componentContext: ComponentContext,
            val reportId: Long?,
            val navigateBack: () -> Unit
        ) : Child()
    }
}

