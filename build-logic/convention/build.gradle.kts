import org.jetbrains.kotlin.gradle.dsl.JvmTarget
//**base foundation to share Gradle configuration

plugins {
    // let create and reference conventions plugins here, recognize module as build-logic build
    `kotlin-dsl`
}

//similar to package name
group = "com.example.convention.buildlogic"

dependencies {

    //only used at compile time
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.androidx.room.gradle.plugin)
    implementation(libs.buildkonfig.compiler)
    implementation(libs.buildkonfig.gradlePlugin)
}

//jdk target - version of java that can be used here
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

tasks{
    //variation of lint task
    validatePlugins{
        enableStricterValidation = true
        failOnWarning = true
    }
}

//register own plugins
gradlePlugin {
    plugins {
        //reference plugin in project
        register("androidApplication"){
            id = "com.example.chirpappkmp.convention.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidComposeApplication"){
            id = "com.example.chirpappkmp.convention.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("cmpApplication"){
            id = "com.example.chirpappkmp.convention.cmp.application"
            implementationClass = "CmpApplicationConventionPlugin"
        }
        register("kmpLibrary"){
            id = "com.example.chirpappkmp.convention.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("cmpLibrary"){
            id = "com.example.chirpappkmp.convention.cmp.library"
            implementationClass = "CmpLibraryConventionPlugin"
        }
        register("cmpFeature"){
            id = "com.example.chirpappkmp.convention.cmp.feature"
            implementationClass = "CmpFeatureConventionPlugin"
        }
        register("buildKonfig"){
            id = "com.example.chirpappkmp.convention.buildkonfig"
            implementationClass = "BuildKonfigConventionPlugin"
        }
        register("room"){
            id = "com.example.chirpappkmp.convention.room"
            implementationClass = "RoomConventionPlugin"
        }
    }
}
