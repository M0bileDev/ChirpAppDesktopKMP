import com.android.build.api.dsl.ApplicationExtension
import com.example.chirpappkmp.convention.configureKotlinAndroid
import com.example.chirpappkmp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {

    //executed the moment applied in plugins block
    override fun apply(target: Project) {

        //Gradle project
        with(target) {

            //let apply different plugins
            with(pluginManager) {

                //plugin that has to be applied, when module will be recognized
                //as android application module, applied along with AndroidApplicationConventionPlugin
                apply("com.android.application")
            }

            //configure android root block -> android{}
            extensions.configure<ApplicationExtension> {
                namespace = "com.example.chirpappkmp"

                defaultConfig {
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()
                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()
                }
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }

                //this -> application extension or KMP android library extension
                configureKotlinAndroid(this)
            }
        }
    }
}