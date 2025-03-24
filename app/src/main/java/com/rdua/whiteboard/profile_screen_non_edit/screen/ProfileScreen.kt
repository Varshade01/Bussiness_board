package com.rdua.whiteboard.profile_screen_non_edit.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.rdua.whiteboard.R
import com.rdua.whiteboard.common.composable.HomeTopBar
import com.rdua.whiteboard.common.composable.HyperText
import com.rdua.whiteboard.data.entities.users.Gender
import com.rdua.whiteboard.data.entities.users.getResId
import com.rdua.whiteboard.profile_screen_non_edit.events.ProfileScreenEvent
import com.rdua.whiteboard.profile_screen_non_edit.state.UserUI
import com.rdua.whiteboard.profile_screen_non_edit.viewmodel.ProfileScreenViewModel
import com.rdua.whiteboard.ui.theme.MaterialTheme
import com.rdua.whiteboard.ui.theme.WhiteBoardTheme

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    ProfileScreen(
        user = viewModel.user,
        onEvent = viewModel::onEvent
    )
}

@Composable
internal fun ProfileScreen(
    user: UserUI,
    onEvent: (ProfileScreenEvent) -> Unit = {}
) {

    val scrollState = rememberScrollState()

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                onEvent(ProfileScreenEvent.LoadUser)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier.background(Color.White)
    ) {
        HomeTopBar(titleTopBar = stringResource(R.string.profile_title))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(horizontal = MaterialTheme.spaces.space4)
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space6))

                AsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally),
                    model = user.photoUrl,
                    fallback = painterResource(R.drawable.default_user_picture),
                    placeholder = painterResource(R.drawable.default_user_picture),
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(R.string.profile_picture_description)
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space6))

                Column(
                    modifier = Modifier
                        .border(0.5.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .padding(MaterialTheme.spaces.space4)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.name),
                            fontWeight = FontWeight(600)
                        )
                        Text(
                            text = user.name,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.spaces.space6),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.email),
                            fontWeight = FontWeight(600)
                        )
                        Text(
                            text = user.email,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = MaterialTheme.spaces.space6),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.mobile_number),
                            fontWeight = FontWeight(600)
                        )
                        LoadDataOrLinkButton(
                            inputData = user.mobileNumber,
                            stringResource = R.string.add_number,
                            onEvent
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.gender),
                            fontWeight = FontWeight(600)
                        )
                        LoadDataOrLinkButton(
                            inputData = stringResource(id = user.gender.getResId()),
                            stringResource = R.string.update_gender,
                            onEvent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space4))

                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    onClick = {
                        onEvent(ProfileScreenEvent.NavigateToProfileEditScreen)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.edit)
                    )
                }
            }
        }
    }
}

@Composable
fun LoadDataOrLinkButton(
    inputData: String?,
    stringResource: Int,
    onEvent: (ProfileScreenEvent) -> Unit
) {
    if (!inputData.isNullOrEmpty() && inputData != stringResource(id = Gender.PREFER_NOT_TO_SAY.getResId())) {
        Text(
            text = inputData,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    } else {
        HyperText(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd,
            hyperTextStyle = MaterialTheme.typography.bodyLarge,
            content = listOf(
                HyperText.Link(
                    text = stringResource(stringResource),
                    onClick = {
                        onEvent(ProfileScreenEvent.NavigateToProfileEditScreen)
                    }
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    WhiteBoardTheme {
        Surface {
            val user = UserUI()
            ProfileScreen(user = user)
        }
    }
}