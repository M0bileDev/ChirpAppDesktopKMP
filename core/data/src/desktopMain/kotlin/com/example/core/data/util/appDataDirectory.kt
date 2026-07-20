package com.example.core.data.util

import com.example.core.data.auth.APP_DATA
import com.example.core.data.auth.APP_FOLDER_NAME
import com.example.core.data.auth.HOME_DIRECTORY
import com.example.core.data.auth.LINUX_APPLICATIONS_DIRECTORY
import com.example.core.data.auth.MAC_APPLICATIONS_DIRECTORY
import java.io.File

val applicationDirectory: File
    get() {
        val userHomeDirectory = System.getProperty(HOME_DIRECTORY)
        return when (currentOS) {
            DesktopOS.WINDOWS ->
                File(System.getenv(APP_DATA), APP_FOLDER_NAME)

            DesktopOS.MACOS -> File(
                userHomeDirectory,
                buildString { MAC_APPLICATIONS_DIRECTORY + APP_FOLDER_NAME })

            DesktopOS.LINUX -> File(
                userHomeDirectory,
                buildString { LINUX_APPLICATIONS_DIRECTORY + APP_FOLDER_NAME })
        }
    }