package com.rdua.whiteboard.edit_profile.composable

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun rememberPhotoPicker(
    onResult: (Uri?) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia(),
    onResult = onResult
)

@Composable
fun rememberCameraLauncher(
    onResult: (Boolean) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture(), // This requires uri
    onResult = onResult
)

@Composable
fun rememberCameraPermissionLauncher(
    onResult: (Boolean) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission(),
    onResult = onResult
)