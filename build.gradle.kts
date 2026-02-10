import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
}

group = "ru.arheo"
version = "0.0.1-alpha"

repositories {
    mavenCentral()
}

kotlin {
    macosArm64()
    macosX64()
    linuxArm64()
    linuxX64()
    mingwX64()

    targets.withType<KotlinNativeTarget>().forEach { target ->
        target.binaries.executable {
            entryPoint = "main"
        }
    }

    sourceSets {
        nativeMain.dependencies {
            implementation(libs.kotlinxSerializationJson)
        }
    }
}

val nativeTargetBuildTasks: Map<String, String> = mapOf(
    "MacosArm64" to "linkReleaseExecutableMacosArm64",
    "MacosX64" to "linkReleaseExecutableMacosX64",
    "LinuxArm64" to "linkReleaseExecutableLinuxArm64",
    "LinuxX64" to "linkReleaseExecutableLinuxX64",
    "MingwX64" to "linkReleaseExecutableMingwX64"
)

nativeTargetBuildTasks.forEach { (name, taskName) ->
    tasks.register("build$name") {
        group = "native"
        description = "Build executable for $name"
        dependsOn(taskName)
    }
}

tasks.register("buildWindows") {
    group = "native"
    description = "Build executable for Windows (x64)"
    dependsOn("linkReleaseExecutableMingwX64")
}

tasks.register("buildAllNative") {
    group = "native"
    description = "Build executables for all native targets"
    dependsOn(nativeTargetBuildTasks.values.toList())
}
