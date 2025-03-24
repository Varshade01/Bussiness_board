package com.rdua.whiteboard.edit_profile.composable

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.rdua.whiteboard.common.composable.PermissionDialog
import com.rdua.whiteboard.common.composable.RectangleBottomSheet
import com.rdua.whiteboard.common.permission.CameraPermissionInfo
import com.rdua.whiteboard.common.permission.PermissionViewModel
import com.rdua.whiteboard.common.utils.createTempImageFile
import com.rdua.whiteboard.common.utils.getBitmap
import com.rdua.whiteboard.common.utils.openAppSettings
import com.rdua.whiteboard.edit_profile.events.EditProfileEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickPhotoBottomSheet(
    permissionViewModel: PermissionViewModel = hiltViewModel(),
    openBottomSheet: Boolean = false,
    onDismissRequest: () -> Unit = { },
    onEvent: (EditProfileEvent) -> Unit = { }
) {
    val context = LocalContext.current
    var tempFileUri by rememberSaveable { mutableStateOf(Uri.EMPTY) }

    val singlePhotoPickerLauncher = rememberPhotoPicker(
        onResult = { uri ->
            if (uri != null) {
                onEvent(EditProfileEvent.PictureChange(context.getBitmap(uri)))
            }
        }
    )

    val cameraLauncher = rememberCameraLauncher(
        onResult = { photoTaken ->
            if (photoTaken) {
                onEvent(EditProfileEvent.PictureChange(context.getBitmap(tempFileUri)))
            }
        }
    )

    val cameraPermissionResultLauncher = rememberCameraPermissionLauncher(
        onResult = { isGranted ->
            if (isGranted) {
                tempFileUri = context.createTempImageFile()
                cameraLauncher.launch(tempFileUri)
            } else {
                permissionViewModel.onShowPermissionDialog(CameraPermissionInfo)
            }
        }
    )

    RectangleBottomSheet(
        openBottomSheet = openBottomSheet,
        onDismissRequest = onDismissRequest,
    ) {
        OpenGalleryItem(
            onClick = {
                onDismissRequest()
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )

        TakePictureItem(
            onClick = {
                onDismissRequest()
                cameraPermissionResultLauncher.launch(CameraPermissionInfo.permission)
            }
        )
    }

    permissionViewModel.permissionDialogQueue
        .forEach { permissionInfo ->
            PermissionDialog(
                permissionInfo = permissionInfo,
                onDismissRequest = permissionViewModel::dismissDialog,
                onGoToAppSettingsClick = {
                    permissionViewModel.dismissDialog()
                    context.openAppSettings()
                }
            )
        }
}