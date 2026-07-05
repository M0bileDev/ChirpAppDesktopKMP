package com.example.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.core.data.util.applicationDirectory
import java.io.File

const val APP_FOLDER_NAME = "Chirp"
const val HOME_DIRECTORY = "user.home"
const val APP_DATA = "APPDATA"
const val MAC_APPLICATIONS_DIRECTORY = "Library/Application Support/"
const val LINUX_APPLICATIONS_DIRECTORY = ".local/share/"

fun createDataStore(): DataStore<Preferences> = createDataStore {

    if (!applicationDirectory.exists()) {
        applicationDirectory.mkdirs()
    }

    File(applicationDirectory, DATA_STORE_FILE_NAME).absolutePath
}