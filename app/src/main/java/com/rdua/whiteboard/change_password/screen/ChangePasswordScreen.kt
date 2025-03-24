package com.rdua.whiteboard.change_password.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rdua.whiteboard.R
import com.rdua.whiteboard.change_password.events.ChangePasswordEvent
import com.rdua.whiteboard.change_password.state.ChangePasswordUIState
import com.rdua.whiteboard.change_password.viewmodel.ChangePasswordViewModel
import com.rdua.whiteboard.common.composable.HomeTopBar
import com.rdua.whiteboard.common.composable.PasswordField
import com.rdua.whiteboard.ui.theme.MaterialTheme
import com.rdua.whiteboard.ui.theme.WhiteBoardTheme

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    ChangePasswordScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@Composable
internal fun ChangePasswordScreen(
    state: ChangePasswordUIState,
    onEvent: (ChangePasswordEvent) -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .background(Color.White)
    ) {

        HomeTopBar(
            titleTopBar = stringResource(id = R.string.change_password),
            onBackPressed = { onEvent(ChangePasswordEvent.NavigateBack) })

        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(MaterialTheme.spaces.space4)
        ) {


            PasswordField(
                modifier = Modifier.fillMaxWidth(),
                text = state.currentPasswordText,
                isError = state.currentPasswordIsError,
                hint = stringResource(R.string.current_password),
                passwordVisibleDescription = stringResource(R.string.password_description_on),
                passwordInvisibleDescription = stringResource(R.string.password_description_off),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = stringResource(R.string.password_field_description)
                    )
                },
                errorText = state.currentPasswordErrorTextId?.let { id ->
                    stringResource(id = id)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                onValueChange = {
                    onEvent(ChangePasswordEvent.CurrentPasswordChange(it))
                }
            )

            PasswordField(
                modifier = Modifier.fillMaxWidth(),
                text = state.newPasswordText,
                isError = state.newPasswordIsError,
                hint = stringResource(R.string.new_password),
                passwordVisibleDescription = stringResource(R.string.password_description_on),
                passwordInvisibleDescription = stringResource(R.string.password_description_off),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = stringResource(R.string.password_field_description)
                    )
                },
                errorText = state.newPasswordErrorTextId?.let { id ->
                    stringResource(id = id)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                onValueChange = {
                    onEvent(ChangePasswordEvent.NewPasswordChange(it))
                }
            )

            PasswordField(
                modifier = Modifier.fillMaxWidth(),
                text = state.passwordConfirmText,
                isError = state.passwordConfirmIsError,
                hint = stringResource(R.string.confirm_password),
                passwordVisibleDescription = stringResource(R.string.password_description_on),
                passwordInvisibleDescription = stringResource(R.string.password_description_off),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = stringResource(R.string.password_field_description)
                    )
                },
                errorText = state.passwordConfirmErrorTextId?.let { id ->
                    stringResource(id = id)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
                onValueChange = {
                    onEvent(ChangePasswordEvent.PasswordConfirmChange(it))
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                onClick = {
                    onEvent(ChangePasswordEvent.ChangePassword)
                    focusManager.clearFocus()
                }
            ) {
                Text(
                    text = stringResource(R.string.update_btn)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    WhiteBoardTheme {
        Surface {
            val state = ChangePasswordUIState()
            ChangePasswordScreen(state)
        }
    }
}
