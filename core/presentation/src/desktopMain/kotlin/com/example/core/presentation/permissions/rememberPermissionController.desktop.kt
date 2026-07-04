package com.example.core.presentation.permissions

import androidx.compose.runtime.Composable

@Composable
actual fun rememberPermissionController(): PermissionController {
    return PermissionController()
}