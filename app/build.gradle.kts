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
            implementation(projects.feature.reportList.api)
            implementation(projects.feature.reportList.impl)
            implementation(projects.feature.reportEditor.api)
            implementation(projects.feature.reportEditor.impl)
            implementation(projects.feature.reportSelector.api)
            implementation(projects.feature.reportSelector.impl)
            implementation(projects.feature.reportViewer.api)
            implementation(projects.feature.reportViewer.impl)
            
            implementation(libs.bundles.compose)
            implementation(libs.decompose.extensions.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.compose.components.resources)
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

        buildTypes.release {
            proguard {
                isEnabled.set(false)
            }
        }

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Deb,
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
