package com.rdua.whiteboard.board.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.data.UserInfo
import com.rdua.whiteboard.ui.theme.MaterialTheme

/**
 * Composable function to display user information, including an image and a name.
 *
 * @param userInfo The user information to be displayed.
 * @param backgroundColor The color of the rectangle background drawn behind the content.
 */
@Composable
fun UserInfoLabel(
    userInfo: UserInfo,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black.copy(alpha = 0.55f)
) {
    Row(
        modifier = modifier
            .background(backgroundColor)
            .padding(MaterialTheme.spaces.space1),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
            model = userInfo.photoUrl,
            fallback = painterResource(R.drawable.default_user_picture),
            placeholder = painterResource(R.drawable.default_user_picture),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spaces.space2))

        Text(
            text = getUserName(userInfo),
            color = MaterialTheme.colors.textAccent
        )
    }
}

@Composable
private fun getUserName(userInfo: UserInfo): String {
    return when {
        userInfo.name == null -> stringResource(id = R.string.unidentified)
        userInfo.isThisUser -> userInfo.name + stringResource(id = R.string.same_user_postfix)
        else -> userInfo.name
    }
}