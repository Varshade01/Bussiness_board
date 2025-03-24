package com.rdua.whiteboard.edit_profile.composable

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.rdua.whiteboard.R
import com.rdua.whiteboard.common.composable.BottomSheetTextItem

@Composable
fun ChangeProfilePictureItem(
    onClick: () -> Unit
) {
    BottomSheetTextItem(
        text = stringResource(R.string.change_picture),
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_edit_24),
                contentDescription = stringResource(R.string.change_picture_description)
            )
        }
    )
}

@Composable
fun DeleteProfilePictureItem(
    onClick: () -> Unit
) {
    BottomSheetTextItem(
        text = stringResource(R.string.delete_picture),
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                contentDescription = stringResource(R.string.delete_picture_description)
            )
        }
    )
}

@Composable
fun OpenGalleryItem(
    onClick: () -> Unit
) {
    BottomSheetTextItem(
        text = stringResource(R.string.open_gallery),
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_gallery),
                contentDescription = stringResource(R.string.open_gallery_description)
            )
        }
    )
}

@Composable
fun TakePictureItem(
    onClick: () -> Unit
) {
    BottomSheetTextItem(
        text = stringResource(R.string.take_picture),
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_photo_camera_24),
                contentDescription = stringResource(R.string.take_picture_description)
            )
        }
    )
}