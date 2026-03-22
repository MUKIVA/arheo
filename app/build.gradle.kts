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
            implementation(projects.core.api)
            implementation(projects.core.impl)
            implementation(projects.feature.reportList.api)
            implementation(projects.feature.reportList.impl)
            implementation(projects.feature.reportEditor.api)
            implementation(projects.feature.reportEditor.impl)
            implementation(projects.feature.reportSelector.api)
            implementation(projects.feature.reportSelector.impl)
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
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.AppImage,
                TargetFormat.Exe,
            )
            packageName = "Arheo"
            packageVersion = internalVersion
            description = "Управление археологическими отчётами"
            vendor = "Arheo"
            includeAllModules = true
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))

            linux {
                shortcut = true
            }

            windows {
                shortcut = true
                dirChooser = true
            }

            macOS {
                bundleID = internalPackageName
            }
        }
    }
}
