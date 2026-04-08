plugins {
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeCompiler)
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
            api(libs.koin.compose)
        }

        jvmMain.dependencies {
            implementation(libs.exposed.core)
            implementation(libs.exposed.jdbc)
            implementation(libs.sqlite.jdbc)
            implementation(libs.slf4j.nop)
            implementation(libs.apache.commons.compress)
        }
    }
}
