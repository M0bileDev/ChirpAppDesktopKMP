rootProject.name = "ChirpAppKMP"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        //gradle plugins that can be reference inside project
        gradlePluginPortal()
    }
    //reference to version catalog - reference to dependencies
    versionCatalogs {
        create("libs"){
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

include(":convention")

