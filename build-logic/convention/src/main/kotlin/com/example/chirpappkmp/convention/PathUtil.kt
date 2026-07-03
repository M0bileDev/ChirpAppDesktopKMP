package com.example.chirpappkmp.convention

import org.gradle.api.Project
import java.util.Locale

internal fun Project.pathToPackageName(): String {
    val relativePackageName = path.replace(':', '.').lowercase()

    //based on the module path it returns :core:data
    // -> com.example.core.data -> then applied to module namespace
    return "com.example$relativePackageName"
}

//based on the module path it returns :core:data
// -> core_data_my_resource -> then applied to module namespace
internal fun Project.pathToResourcePrefix(): String {
    return path.replace(':', '_').lowercase().drop(1) + "_"
}

internal fun Project.pathToFrameworkName(): String {
    //:core:data -> ["core","data"]
    val parts = this.path.split(":", "_", "-", " ")

    //CoreData
    return parts.joinToString("") { part ->
        part.replaceFirstChar {
            it.titlecase(Locale.ROOT)
        }
    }
}