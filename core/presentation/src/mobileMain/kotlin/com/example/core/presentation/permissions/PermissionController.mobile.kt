package com.example.core.presentation.permissions

import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION

typealias MokoPermission = dev.icerock.moko.permissions.Permission

actual class PermissionController(
    private val mokoPermissionsController: PermissionsController
) {
    actual suspend fun requestPermission(permission: Permission): PermissionState {
        return try {
            mokoPermissionsController.providePermission(
                permission = permission.toMokoPermission()
            )
            PermissionState.GRANTED
        } catch (_: DeniedAlwaysException) {
            PermissionState.PERMANENTLY_DENIED
        } catch (_: DeniedException) {
            PermissionState.DENIED
        } catch (_: RequestCanceledException) {
            PermissionState.DENIED
        } catch (_: Exception){
            PermissionState.DENIED
        }
    }
}

private fun Permission.toMokoPermission(): MokoPermission {
    return when (this) {
        Permission.NOTIFICATIONS -> MokoPermission.REMOTE_NOTIFICATION
    }
}