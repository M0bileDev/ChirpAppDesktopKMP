package com.example.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.core.data.util.DesktopOS
import com.example.core.data.util.currentOS
import java.io.File

const val APP_FOLDER_NAME = "Chirp"
const val HOME_DIRECTORY = "user.home"
const val APP_DATA = "APPDATA"
const val MAC_APPLICATIONS_DIRECTORY = "Library/Application Support/"
const val LINUX_APPLICATIONS_DIRECTORY = ".local/share/"

fun createDataStore(): DataStore<Preferences> = createDataStore {
    val userHomeDirectory = System.getProperty(HOME_DIRECTORY)
    val applicationDirectory = when (currentOS) {
        DesktopOS.WINDOWS -> File(System.getenv(APP_DATA), APP_FOLDER_NAME)
        DesktopOS.MACOS -> File(
            userHomeDirectory,
            buildString { MAC_APPLICATIONS_DIRECTORY + APP_FOLDER_NAME })

        DesktopOS.LINUX -> File(
            userHomeDirectory,
            buildString { LINUX_APPLICATIONS_DIRECTORY + APP_FOLDER_NAME })
    }

    if (!applicationDirectory.exists()) {
        applicationDirectory.mkdirs()
    }

    File(applicationDirectory, DATA_STORE_FILE_NAME).absolutePath
}