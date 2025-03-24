package com.rdua.whiteboard.edit_profile.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rdua.whiteboard.R
import com.rdua.whiteboard.common.composable.PasswordDialog
import com.rdua.whiteboard.common.state.PasswordState
import com.rdua.whiteboard.edit_profile.events.EditProfileEvent

@Composable
fun ConfirmPasswordDialog(
    isOpenedDialog: Boolean,
    passwordState: PasswordState,
    onEvent: (EditProfileEvent) -> Unit,
) {
    if (isOpenedDialog) {
        PasswordDialog(
            titleDialog = stringResource(R.string.change_email),
            textConfirmButton = stringResource(R.string.confirm),
            passwordState = passwordState,
            confirmOnClick = {
                onEvent(EditProfileEvent.ConfirmPassword)
            },
            dismissOnClick = { onEvent(EditProfileEvent.DismissConfirmPasswordDialog) },
            onValueChange = { onEvent(EditProfileEvent.ConfirmPasswordValueChange(it)) },
        )
    }
}