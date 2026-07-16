@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.LibraryExtension
import com.example.chirpappkmp.convention.configureKotlinAndroid
import com.example.chirpappkmp.convention.configureKotlinMultiplatform
import com.example.chirpappkmp.convention.libs
import com.example.chirpappkmp.convention.pathToResourcePrefix
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

//plugin for foundation kmp modules (without cmp)
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("com.android.kotlin.multiplatform.library")
            }

            configureKotlinMultiplatform()

            //special for kmp module, common part for kmp module
            dependencies {

                //corresponds to the source set, when it will be applied to
                //Main -> production source code,
                //for test it will be commonTest (test in common source set) etc.
                "commonMainImplementation"(
                    //plugin also needs dependency for serialization
                    libs.findLibrary("kotlinx-serialization-json").get())
                "commonTestImplementation"(
                    //junit for unit tests
                    libs.findLibrary("kotlin-test").get())
            }
        }
    }
}