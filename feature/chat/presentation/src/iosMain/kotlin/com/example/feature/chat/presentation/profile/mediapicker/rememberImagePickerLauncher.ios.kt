@file:OptIn(ExperimentalForeignApi::class)

package com.example.feature.chat.presentation.profile.mediapicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerConfigurationSelectionOrdered
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UniformTypeIdentifiers.UTType
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_group_create
import platform.darwin.dispatch_group_enter
import platform.darwin.dispatch_group_leave
import platform.darwin.dispatch_group_notify
import platform.posix.memcpy

@Composable
actual fun rememberImagePicker(onResult: (PickedImageData) -> Unit): ImagePickerLauncher {
    val scope = rememberCoroutineScope()
    val delegate = remember {
        object : NSObject(), PHPickerViewControllerDelegateProtocol {
            // picker -> view that displays photos
            // didFinishPicking -> list that contains image results
            override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                // close picker with animation and don't focus when the animation
                // is finished (completion = null)
                picker.dismissViewControllerAnimated(true, null)
                // PHPickerResult -> type of format that contains image data
                val results = didFinishPicking.filterIsInstance<PHPickerResult>()
                // tracking when many async operations succeed or finished
                val dispatchGroup = dispatch_group_create()
                val imageDataList = mutableListOf<PickedImageData>()

                for (result in results) {
                    //use dispatch group created earlier
                    dispatch_group_enter(dispatchGroup)
                    // provides underlying photo
                    val itemProvider = result.itemProvider
                    val typeIdentifiers = itemProvider.registeredTypeIdentifiers

                    val typeIdentifier = typeIdentifiers.firstOrNull() as? String
                    if (typeIdentifier == null) {
                        dispatch_group_leave(dispatchGroup)
                        continue
                    }

                    val mimeType = UTType.typeWithIdentifier(typeIdentifier)?.preferredMIMEType
                    if (mimeType == null) {
                        dispatch_group_leave(dispatchGroup)
                        continue
                    }

                    itemProvider.loadDataRepresentationForTypeIdentifier(
                        typeIdentifier = typeIdentifier
                    ) { nsData, nsError ->
                        scope.launch {
                            nsData?.let { data ->
                                val bytes = ByteArray(size = data.length.toInt())

                                withContext(Dispatchers.Default) {
                                    // copy contents from C pointer that points
                                    // to byte array into Kotlin's ByteArray
                                    memcpy(
                                        bytes.refTo(0),
                                        data.bytes,
                                        data.length
                                    )
                                }

                                imageDataList.add(
                                    PickedImageData(
                                        bytes = bytes,
                                        mimeType = mimeType
                                    )
                                )
                            }
                            dispatch_group_leave(dispatchGroup)
                        }
                    }

                    // dispatchGroup has finished processing, so
                    // all image results have been processed here,
                    // and onResult lambda has to be notified
                    dispatch_group_notify(
                        dispatchGroup,
                        dispatch_get_main_queue()
                    ) {
                        scope.launch {
                            imageDataList.firstOrNull()?.let { item ->
                                onResult(item)
                            }
                        }
                    }
                }
            }
        }
    }

    return remember {
        // create picker view controller with configuration:
        // only one item can be selected
        // items has to be an image
        // the results array comes back in that same tap order
        val pickerViewController = PHPickerViewController(
            configuration = PHPickerConfiguration().apply {
                setSelectionLimit(1)
                setFilter(PHPickerFilter.imagesFilter)
                setSelection(PHPickerConfigurationSelectionOrdered)
            }
        )
        pickerViewController.delegate = delegate

        ImagePickerLauncher(
            onLaunch = {
                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    pickerViewController,
                    true,
                    null
                )
            }
        )
    }
}