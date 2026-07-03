import com.android.build.api.dsl.ApplicationExtension
import com.example.chirpappkmp.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            //configuration for android application module that uses compose
            with(pluginManager) {

                //working with application module -> needs application plugin
                //apply("com.android.application") instead use below plugin,
                //because it already has shared configuration
                apply("com.example.chirpappkmp.convention.android.application")
                //necessary to work with kmp
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}