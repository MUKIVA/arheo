package ru.arheo.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.arheo.feature.report_editor.presentation.ReportEditorComponent
import ru.arheo.feature.report_list.presentation.ReportListComponent

interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class ReportListChild(val component: ReportListComponent) : Child()
        data class ReportEditorChild(val component: ReportEditorComponent) : Child()
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val reportListFactory: ReportListComponent.Factory,
    private val reportEditorFactory: ReportEditorComponent.Factory,
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
    ): RootComponent.Child =
        when (config) {
            is Config.ReportList -> RootComponent.Child.ReportListChild(
                reportListFactory.create(
                    componentContext = componentContext,
                    output = ::onReportListOutput,
                ),
            )
            is Config.ReportEditor -> RootComponent.Child.ReportEditorChild(
                reportEditorFactory.create(
                    componentContext = componentContext,
                    reportId = config.reportId,
                    output = ::onReportEditorOutput,
                ),
            )
        }

    @OptIn(DelicateDecomposeApi::class)
    private fun onReportListOutput(output: ReportListComponent.Output) {
        when (output) {
            is ReportListComponent.Output.CreateReport -> navigation.push(Config.ReportEditor(reportId = null))
            is ReportListComponent.Output.EditReport -> navigation.push(Config.ReportEditor(reportId = output.reportId))
        }
    }

    private fun onReportEditorOutput(output: ReportEditorComponent.Output) {
        when (output) {
            is ReportEditorComponent.Output.Saved -> navigation.pop()
            is ReportEditorComponent.Output.Cancelled -> navigation.pop()
        }
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object ReportList : Config

        @Serializable
        data class ReportEditor(val reportId: Long? = null) : Config
    }
}
