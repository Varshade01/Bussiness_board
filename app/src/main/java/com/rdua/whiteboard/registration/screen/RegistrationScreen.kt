package com.rdua.whiteboard.registration.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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
import com.rdua.whiteboard.common.composable.PasswordField
import com.rdua.whiteboard.registration.events.RegistrationEvent
import com.rdua.whiteboard.registration.state.RegistrationUIState
import com.rdua.whiteboard.registration.viewmodel.RegistrationViewModel
import com.rdua.whiteboard.ui.theme.MaterialTheme
import com.rdua.whiteboard.ui.theme.WhiteBoardTheme

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    RegistrationScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@Composable
internal fun RegistrationScreen(
    state: RegistrationUIState,
    onEvent: (RegistrationEvent) -> Unit = {},
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.safeDrawingPadding()
    ) {
        BackButtonTopBar(
            onBackPressed = {
                onEvent(RegistrationEvent.NavigateBack)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.spaces.space4),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.registration_title),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = stringResource(R.string.registration_paragraph),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spaces.space3))

            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space3))

                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.nameText,
                    isError = state.nameIsError,
                    errorText = state.nameErrorTextId?.let { id ->
                        stringResource(id = id)
                    },
                    hint = stringResource(R.string.full_name_field_hint),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = stringResource(R.string.full_name_field_description)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                    onValueChange = {
                        onEvent(RegistrationEvent.NameChange(it))
                    }
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
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                    onValueChange = {
                        onEvent(RegistrationEvent.EmailChange(it))
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space4))

                PasswordField(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.passwordText,
                    isError = state.passwordIsError,
                    hint = stringResource(R.string.password_field_hint),
                    passwordVisibleDescription = stringResource(R.string.password_description_on),
                    passwordInvisibleDescription = stringResource(R.string.password_description_off),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = stringResource(R.string.password_field_description)
                        )
                    },
                    errorText = state.passwordErrorTextId?.let { id ->
                        stringResource(id = id)
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                    onValueChange = {
                        onEvent(RegistrationEvent.PasswordChange(it))
                    }
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space4))

                PasswordField(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.passwordConfirmText,
                    isError = state.passwordConfirmIsError,
                    hint = stringResource(R.string.password_confirm_field_hint),
                    passwordVisibleDescription = stringResource(R.string.password_description_on),
                    passwordInvisibleDescription = stringResource(R.string.password_description_off),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = stringResource(R.string.password_confirm_field_description)
                        )
                    },
                    errorText = state.passwordConfirmErrorTextId?.let { id ->
                        stringResource(id = id)
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions {
                        onEvent(RegistrationEvent.Register)
                        focusManager.clearFocus()
                    },
                    onValueChange = {
                        onEvent(RegistrationEvent.PasswordConfirmChange(it))
                    }
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    onClick = {
                        onEvent(RegistrationEvent.Register)
                        focusManager.clearFocus()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.sign_up)
                    )
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spaces.space5))

                HyperText(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                    content = listOf(
                        HyperText.Text(
                            text = stringResource(R.string.registration_hypertext)
                        ),
                        HyperText.Space,
                        HyperText.Link(
                            text = stringResource(R.string.registration_hyperlink),
                            onClick = {
                                onEvent(RegistrationEvent.NavigateToLogin)
                            }
                        )
                    )
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
            val state = RegistrationUIState()
            RegistrationScreen(state = state)
        }
    }
}