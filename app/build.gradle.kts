import org.jetbrains.compose.desktop.application.dsl.TargetFormat

private val internalVersion = "1.0.0"
private val internalPackageName = "ru.arheo"

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinxSerialization)
}

group = internalPackageName
version = internalVersion

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.feature.reportList)
            implementation(projects.feature.reportEditor)
            implementation(libs.bundles.compose)
            implementation(libs.decompose.extensions.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "$internalPackageName.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Dmg)
            packageName = internalPackageName
            packageVersion = internalVersion
        }
    }
}
