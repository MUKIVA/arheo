plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            api(projects.core.api)
            api(projects.feature.reportSelector.api)
        }
    }
}
