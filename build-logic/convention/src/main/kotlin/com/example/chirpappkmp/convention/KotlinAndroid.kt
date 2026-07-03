package com.example.chirpappkmp.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

//Configure Kotlin language for the Android side
internal fun Project.configureKotlinAndroid(
    //for KMP it has to be as CommonExtension, common parts of the Android specific
    // config for both Android and KMP modules
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    with(commonExtension) {
        compileSdk = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()
        //for both library module and application module
        defaultConfig.minSdk = libs.findVersion("projectMinSdkVersion").get().toString().toInt()

        //for each module
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17

            //convert code from higher sdk (like sdk 26 java.DateTime) to equivalent for lower sdk (like sdk 24)
            isCoreLibraryDesugaringEnabled = true
        }

        configureKotlin()

        //automatically applied when function will be called
        dependencies {
            //equivalent to implementation from the normal Gradle module
            add(
                "coreLibraryDesugaring",
                libs.findLibrary("android-desugarJdkLibs").get()
            )
            //alternative
            //"coreLibraryDesugaring"(libs.findLibrary("android-desugarJdkLibs").get())
        }
    }
}

//available to use in regular kotlin module
internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)

            freeCompilerArgs.add(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
            )
        }
    }
}