package com.rdua.whiteboard.invite_via_email.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board_options_dialog.event.BoardOptionsEvent
import com.rdua.whiteboard.common.composable.ButtonDialog
import com.rdua.whiteboard.common.composable.InputField
import com.rdua.whiteboard.invite_via_email.state.InviteViaEmailToBoardUIState
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun InviteToBoardViaEmailDialog(
    state: InviteViaEmailToBoardUIState,
    isOpenedDialog: Boolean,
    titleDialog: String = stringResource(id = R.string.invite_via_email),
    textDialog: String = stringResource(id = R.string.enter_the_user_email_subtitle),
    textConfirmButton: String = stringResource(id = R.string.confirm_invite),
    onEvent: (BoardOptionsEvent) -> Unit = { },
) {
    if (isOpenedDialog) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                ButtonDialog(
                    textButton = textConfirmButton,
                    onClick = { onEvent(BoardOptionsEvent.InviteViaEmail) }
                )
            },
            dismissButton = {
                ButtonDialog(
                    textButton = stringResource(R.string.cancel),
                    onClick = { onEvent(BoardOptionsEvent.CloseInviteToBoardDialog) }
                )
            },
            title = {
                Text(
                    text = titleDialog,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Column {
                    Text(
                        text = textDialog,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(10.dp)
                    )

                    InputField(
                        text = state.email,
                        isError = state.isEmailError,
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
                        ),
                        onValueChange = {
                            onEvent(BoardOptionsEvent.EnterInviteViaEmailText(it))
                        }
                    )
                }
            },
            containerColor = Color.White,
        )
    }
}