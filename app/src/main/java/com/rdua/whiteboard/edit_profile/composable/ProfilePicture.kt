package com.rdua.whiteboard.edit_profile.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rdua.whiteboard.R
import com.rdua.whiteboard.edit_profile.events.EditProfileEvent
import com.rdua.whiteboard.ui.theme.MaterialTheme

private val profilePictureSize = 100.dp
private val editIconOffsetX = 40.dp
private val editIconOffsetY = 60.dp

@Composable
fun ProfilePicture(
    photoUrl: String?,
    onEvent: (EditProfileEvent) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spaces.space6),
        contentAlignment = Alignment.TopCenter
    ) {
        AsyncImage(
            modifier = Modifier
                .size(profilePictureSize)
                .clip(CircleShape),
            model = photoUrl,
            fallback = painterResource(R.drawable.default_user_picture),
            placeholder = painterResource(R.drawable.default_user_picture),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.profile_picture_description)
        )

        RoundIconButton(
            modifier = Modifier.offset(editIconOffsetX, editIconOffsetY),
            onClick = {
                onEvent(EditProfileEvent.CheckEditIconAction)
            }
        ) {
            Icon(
                imageVector = if (photoUrl == null) {
                    ImageVector.vectorResource(R.drawable.ic_photo_camera_16)
                } else {
                    ImageVector.vectorResource(R.drawable.ic_edit_16)
                },
                contentDescription = stringResource(R.string.profile_icon_description)
            )
        }
    }
}