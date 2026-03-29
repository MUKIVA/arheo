package ru.arheo.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class ReportList(val componentContext: ComponentContext) : Child()
        data class ReportEditor(val componentContext: ComponentContext) : Child()
    }
}

