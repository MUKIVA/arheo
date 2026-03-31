package ru.arheo.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.ReportList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child = when (config) {
        is Config.ReportList -> RootComponent.Child.ReportList(
            componentContext = componentContext,
            navigateCreateReport = {
                navigation.push(Config.ReportEditor(null))
            },
            navigateEditReport = { reportId ->
                navigation.push(Config.ReportEditor(reportId))
            }

        )
        is Config.ReportEditor -> RootComponent.Child.ReportEditor(
            componentContext = componentContext,
            reportId = config.reportId,
            navigateBack = { navigation.pop() }
        )
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object ReportList : Config

        @Serializable
        data class ReportEditor(val reportId: Long? = null) : Config
    }
}