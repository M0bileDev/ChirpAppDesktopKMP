package com.example.chirpappkmp.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

//configure compose part for android application module or android library
internal fun Project.configureAndroidCompose(
    applicationExtension: ApplicationExtension
) {
    with(applicationExtension) {
        buildFeatures {
            compose = true
        }

        //android side
        dependencies {
            //get real underlying dependency
            val bom = libs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(bom))
            "testImplementation"(platform(bom))
            //enable compose preview, not needed but helpful for android side
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }
}