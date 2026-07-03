package com.example.chirpappkmp.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureIosTargets() {
    extensions.configure<KotlinMultiplatformExtension>{
        listOf(
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                //name of generated framework for iOS
                baseName = "ComposeApp"
                isStatic = true
            }
        }
    }
}