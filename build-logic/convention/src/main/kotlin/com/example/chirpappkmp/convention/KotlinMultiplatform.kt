package com.example.chirpappkmp.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinMultiplatform() {
    //library and application module
    configureAndroidLibraryTarget()
    // enable desktopMain target for other modules
    configureDesktopTarget()

    extensions.configure<KotlinMultiplatformExtension> {
        extensions.configure<KotlinMultiplatformAndroidLibraryExtension> {
            compileSdk = 36
            minSdk = 26
            namespace = pathToPackageName()
            experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
        }

        listOf(
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                //derive framework name dynamically, so convert module path :core:domain -> CoreData
                baseName = this@configureKotlinMultiplatform.pathToFrameworkName()
            }
        }

        applyHierarchyTemplate()

        //additional flags to the compiler
        compilerOptions {
            //suppresses warnings when using expect/actual class declarations in KMP
            freeCompilerArgs.add("-Xexpect-actual-classes")
            //enables usage of APIs annotated with @RequiresOptIn without per-site opt-in
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            //enables usage of experimental Kotlin Time APIs (e.g., Duration, measureTime) without per-site opt-in
            freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        }
    }
}