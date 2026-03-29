package ru.arheo.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
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

    private fun createChild(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child = when (config) {
        is Config.ReportList -> RootComponent.Child.ReportList(componentContext)
        is Config.ReportEditor -> RootComponent.Child.ReportEditor(componentContext)
    }

//    @OptIn(DelicateDecomposeApi::class)
//    private fun onReportListOutput(output: ReportListComponent.Output) {
//        when (output) {
//            is ReportListComponent.Output.CreateReport -> navigation.push(Config.ReportEditor(reportId = null))
//            is ReportListComponent.Output.EditReport -> navigation.push(Config.ReportEditor(reportId = output.reportId))
//        }
//    }

//    private fun onReportEditorOutput(output: ReportEditorComponent.Output) {
//        when (output) {
//            is ReportEditorComponent.Output.Saved -> navigation.pop()
//            is ReportEditorComponent.Output.Cancelled -> navigation.pop()
//        }
//    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object ReportList : Config

        @Serializable
        data class ReportEditor(val reportId: Long? = null) : Config
    }
}