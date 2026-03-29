package ru.arheo.feature.report.selector.di

import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import java.util.UUID

internal class FeatureScope : KoinScopeComponent {
    override val scope: Scope by lazy {
        createScope(
            scopeId = UUID.randomUUID().toString(),
            source = this
        )
    }
}