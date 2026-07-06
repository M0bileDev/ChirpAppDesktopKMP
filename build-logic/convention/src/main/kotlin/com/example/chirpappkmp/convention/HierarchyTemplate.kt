@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package com.example.chirpappkmp.convention

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

// our own hierarchy, groups, default groups like iosMain, androidMain, desktopMain
private val hierarchyTemplate = KotlinHierarchyTemplate {
    //define all the templates (*main) for each variant (android*, ios*, desktop*)
    withSourceSetTree(
        KotlinSourceSetTree.main,
        KotlinSourceSetTree.test
    )

    // representation of commonMain (source sets depends on common)
    common {

        //allow custom compilations
        withCompilations { true }

        group("mobile") {
            withAndroidTarget()
            // it has to be a group because ios bundles multiple
            // source-sets (e.g. different cpu architectures)
            group("ios") {
                withIos()
            }
        }

        // group to share jvm/kotlin common code
        group("jvmCommon") {
            withAndroidTarget()
            withJvm()
        }

        // which source-set, belongs to this group, this group covers all native platforms
        group("native") {
            withNative()

            group("apple")
            withApple()

            group("ios") {
                // this ios group involves certain set of source-sets
                // Gradle -> generates iosMain, iosTest source-sets for every module that
                // apply hierarchyTemplate
                withIos() // access classes from apple source-set
            }
            group("macOS") {
                // this macOS group involves certain set of source-sets
                // access classes from apple source-set but,
                // not from ios source-set (hierarchy matters)
                withMacos()
            }
        }
    }
}

fun KotlinMultiplatformExtension.applyHierarchyTemplate() {
    applyHierarchyTemplate(hierarchyTemplate)
}