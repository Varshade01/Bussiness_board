package com.rdua.whiteboard.edit_profile.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rdua.whiteboard.R
import com.rdua.whiteboard.common.composable.ApproveDialog
import com.rdua.whiteboard.common.composable.HomeTopBar
import com.rdua.whiteboard.common.composable.InputField
import com.rdua.whiteboard.common.state.PasswordState
import com.rdua.whiteboard.data.entities.users.Gender
import com.rdua.whiteboard.edit_profile.actions.EditProfileActions
import com.rdua.whiteboard.edit_profile.composable.ChangePhotoBottomSheet
import com.rdua.whiteboard.edit_profile.composable.ConfirmPasswordDialog
import com.rdua.whiteboard.edit_profile.composable.PickPhotoBottomSheet
import com.rdua.whiteboard.edit_profile.composable.ProfilePicture
import com.rdua.whiteboard.edit_profile.events.EditProfileEvent
import com.rdua.whiteboard.edit_profile.state.EditProfileUIState
import com.rdua.whiteboard.edit_profile.viewmodel.EditProfileViewModel
import com.rdua.whiteboard.ui.theme.MaterialTheme
import com.rdua.whiteboard.ui.theme.WhiteBoardTheme
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    EditProfileScreen(
        state = viewModel.state,
        actionFlow = viewModel.actionFlow,
        onEvent = viewModel::onEvent
    )
}

@Composable
internal fun EditProfileScreen(
    state: EditProfileUIState,
    actionFlow: SharedFlow<EditProfileActions>? = null,
    onEvent: (EditProfileEvent) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    BackPressHandler(onEvent)

    ActionHandler(actionFlow, onEvent)

    UnsavedChangesDialog(state, onEvent)

    ConfirmPasswordDialog(state, onEvent)

    Column(
        modifier = Modifier.background(Color.White)
    ) {
        HomeTopBar(
            titleTopBar = stringResource(R.string.edit_profile),
            onBackPressed = { onEvent(EditProfileEvent.NavigateBack) }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(MaterialTheme.spaces.space4)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .weight(1f)
            ) {
                ProfilePicture(
                    photoUrl = state.photoUrl,
                    onEvent = onEvent
                )

                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.name,
                    hint = stringResource(id = R.string.name_field_hint),
                    isError = state.nameIsError,
                    errorText = state.nameErrorTextId?.let {
                        stringResource(id = it)
                    },
                    onValueChange = {
                        onEvent(EditProfileEvent.NameValueChange(it))
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space1))

                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.email,
                    hint = stringResource(id = R.string.email_field_hint),
                    isError = state.emailIsError,
                    errorText = state.emailErrorTextId?.let {
                        stringResource(id = it)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = {
                        onEvent(EditProfileEvent.EmailValueChange(it))
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space1))

                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.mobile ?: "",
                    hint = stringResource(id = R.string.mobile_field_hint),
                    isError = state.mobileIsError,
                    errorText = state.mobileErrorTextId?.let {
                        stringResource(id = it)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    onValueChange = {
                        onEvent(EditProfileEvent.MobileValueChange(it))
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space6))

                Text(
                    text = stringResource(id = R.string.gender_field_hint),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space4))

                GenderButton(
                    isSelected = state.gender == Gender.MALE,
                    text = stringResource(id = R.string.gender_male),
                    textColor = if (state.gender == Gender.MALE) MaterialTheme.colors.textHighlighted else Color.Black,
                    onClick = {
                        onEvent(EditProfileEvent.GenderValueChange(Gender.MALE))
                    },
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space3))

                GenderButton(
                    isSelected = state.gender == Gender.FEMALE,
                    text = stringResource(id = R.string.gender_female),
                    textColor = if (state.gender == Gender.FEMALE) MaterialTheme.colors.textHighlighted else Color.Black,
                    onClick = {
                        onEvent(EditProfileEvent.GenderValueChange(Gender.FEMALE))
                    },
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space3))

                GenderButton(
                    isSelected = state.gender == Gender.PREFER_NOT_TO_SAY,
                    text = stringResource(id = R.string.gender_prefer_not_to_say),
                    textColor = if (state.gender == Gender.PREFER_NOT_TO_SAY) MaterialTheme.colors.textHighlighted else Color.Black,
                    onClick = {
                        onEvent(EditProfileEvent.GenderValueChange(Gender.PREFER_NOT_TO_SAY))
                    },
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                onClick = {
                    onEvent(EditProfileEvent.SaveChanges)
                },
                enabled = state.isSaveAvailable
            ) {
                Text(text = stringResource(id = R.string.save), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun GenderButton(
    isSelected: Boolean,
    text: String,
    textColor: Color = Color.Black,
    onClick: () -> Unit = {},
) {
    Row(verticalAlignment = CenterVertically) {
        RadioButton(
            selected = isSelected,
            modifier = Modifier
                .size(MaterialTheme.spaces.space6)
                .padding(end = MaterialTheme.spaces.space2),
            onClick = onClick
        )

        Text(
            text = text,
            color = textColor
        )
    }
}

@Composable
fun BackPressHandler(onEvent: (EditProfileEvent) -> Unit) {
    BackHandler {
        onEvent(EditProfileEvent.NavigateBack)
    }
}

@Composable
fun ActionHandler(
    actionFlow: SharedFlow<EditProfileActions>? = null,
    onEvent: (EditProfileEvent) -> Unit = {}
) {
    var openPickPhotoBottomSheet by rememberSaveable { mutableStateOf(false) }
    var openChangePhotoBottomSheet by rememberSaveable { mutableStateOf(false) }

    if (actionFlow != null) {
        LaunchedEffect(Unit) {
            actionFlow.collect { action ->
                when (action) {
                    is EditProfileActions.PickProfilePhoto -> {
                        openPickPhotoBottomSheet = true
                    }

                    is EditProfileActions.ChangeProfilePhoto -> {
                        openChangePhotoBottomSheet = true
                    }
                }
            }
        }
    }

    PickPhotoBottomSheet(
        openBottomSheet = openPickPhotoBottomSheet,
        onDismissRequest = { openPickPhotoBottomSheet = false },
        onEvent = onEvent
    )

    ChangePhotoBottomSheet(
        openBottomSheet = openChangePhotoBottomSheet,
        onDismissRequest = { openChangePhotoBottomSheet = false },
        onPickPhotoBottomSheetRequest = { openPickPhotoBottomSheet = true },
        onEvent = onEvent
    )
}

@Composable
private fun UnsavedChangesDialog(
    state: EditProfileUIState,
    onEvent: (EditProfileEvent) -> Unit = {}
) {
    if (state.showUnsavedChangesDialog) {
        ApproveDialog(
            titleDialog = stringResource(id = R.string.discard_changes_question),
            textDialog = stringResource(id = R.string.unsaved_changes_dialog_text),
            textConfirmButton = stringResource(id = R.string.save_changes),
            textDismissButton = stringResource(id = R.string.discard_changes),
            dismissOnClick = {
                onEvent(EditProfileEvent.NavigateBackDiscardingChanges)
            },
            confirmOnClick = {
                onEvent(EditProfileEvent.NavigateBackSavingChanges)
            },
        )
    }
}

@Composable
private fun ConfirmPasswordDialog(
    state: EditProfileUIState,
    onEvent: (EditProfileEvent) -> Unit = {}
) {
    ConfirmPasswordDialog(
        isOpenedDialog = state.showConfirmPasswordDialog,
        passwordState = PasswordState(
            passwordText = state.confirmPasswordText,
            passwordIsError = state.confirmPasswordIsError,
            passwordErrorTextId = state.confirmPasswordErrorTextId,
        ),
        onEvent = onEvent
    )
}

@Preview
@Composable
private fun Preview() {
    WhiteBoardTheme {
        Surface {
            EditProfileScreen()
        }
    }
}