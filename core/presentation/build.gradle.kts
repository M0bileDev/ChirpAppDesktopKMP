plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(projects.core.domain)
                implementation(compose.components.resources)
                implementation(libs.material3.adaptive)
                implementation(libs.bundles.koin.common)
                implementation(libs.jetbrains.lifecycle.compose)
            }
        }

        val mobileMain by getting {
            // custom source set for mobile only - notification moko library provide
            // implementation only for Android and iOS
            dependencies {
                implementation(libs.moko.permissions)
                implementation(libs.moko.permissions.compose)
                implementation(libs.moko.permissions.notifications)
            }
        }

        androidMain {
            dependsOn(mobileMain)
        }
    }
}