package com.rdua.whiteboard.edit_profile.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.rdua.whiteboard.common.composable.RectangleBottomSheet
import com.rdua.whiteboard.edit_profile.events.EditProfileEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePhotoBottomSheet(
    openBottomSheet: Boolean = false,
    onDismissRequest: () -> Unit = { },
    onPickPhotoBottomSheetRequest: () -> Unit = { },
    onEvent: (EditProfileEvent) -> Unit = { }
) {
    RectangleBottomSheet(
        openBottomSheet = openBottomSheet,
        onDismissRequest = onDismissRequest,
    ) {
        ChangeProfilePictureItem(
            onClick = {
                onDismissRequest()
                onPickPhotoBottomSheetRequest()
            }
        )

        DeleteProfilePictureItem(
            onClick = {
                onDismissRequest()
                onEvent(EditProfileEvent.PictureDelete)
            }
        )
    }
}