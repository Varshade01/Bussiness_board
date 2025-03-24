package com.rdua.whiteboard.reset_password.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rdua.whiteboard.R
import com.rdua.whiteboard.common.composable.BackButtonTopBar
import com.rdua.whiteboard.common.composable.HyperText
import com.rdua.whiteboard.common.composable.InputField
import com.rdua.whiteboard.reset_password.event.ResetPasswordEvent
import com.rdua.whiteboard.reset_password.state.ResetPasswordUIState
import com.rdua.whiteboard.reset_password.viewmodel.ResetPasswordViewModel
import com.rdua.whiteboard.ui.theme.MaterialTheme
import com.rdua.whiteboard.ui.theme.WhiteBoardTheme

@Composable
fun ResetPasswordScreen(viewModel: ResetPasswordViewModel = hiltViewModel()) {

    ResetPasswordScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@Composable
internal fun ResetPasswordScreen(
    state: ResetPasswordUIState,
    onEvent: (ResetPasswordEvent) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.safeDrawingPadding()
    ) {
        BackButtonTopBar(
            onBackPressed = {
                onEvent(ResetPasswordEvent.NavigateBack)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spaces.space4),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.forgot_password),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(id = R.string.reset_password_instructions),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spaces.space4))

            InputField(
                modifier = Modifier.fillMaxWidth(),
                text = state.emailText,
                isError = state.emailIsError,
                hint = stringResource(R.string.email_field_hint),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        contentDescription = stringResource(R.string.email_field_description)
                    )
                },
                errorText = state.emailErrorTextId?.let { id ->
                    stringResource(id = id)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions {
                    onEvent(ResetPasswordEvent.ResetPassword)
                    focusManager.clearFocus()
                },
                onValueChange = {
                    onEvent(ResetPasswordEvent.EmailChange(it))
                }
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                onClick = {
                    onEvent(ResetPasswordEvent.ResetPassword)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.send_instructions)
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spaces.space4))

            HyperText(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
                content = listOf(
                    HyperText.Text(
                        text = stringResource(R.string.remember_password)
                    ),
                    HyperText.Space,
                    HyperText.Link(
                        text = stringResource(R.string.registration_hyperlink),
                        onClick = {
                            onEvent(ResetPasswordEvent.NavigateToLogin)
                        }
                    )
                )
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    WhiteBoardTheme {
        Surface {
            val state = ResetPasswordUIState()
            ResetPasswordScreen(state)
        }
    }
}