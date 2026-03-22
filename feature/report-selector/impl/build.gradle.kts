plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.api)
            implementation(projects.feature.reportSelector.api)
            implementation(libs.bundles.mvikotlin)
            implementation(libs.decompose.core)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
