plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            api(libs.bundles.mvikotlin)
            api(libs.decompose.core)
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.serialization.json)
            api(libs.koin.core)
        }
    }
}
