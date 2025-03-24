package com.rdua.whiteboard.common.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.R
import com.rdua.whiteboard.common.state.PasswordState
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun PasswordDialog(
    titleDialog: String = stringResource(R.string.change_password),
    textDialog: String = stringResource(R.string.enter_current_password_to_confirm),
    textConfirmButton: String = stringResource(R.string.ok),
    passwordState: PasswordState,
    onDismissRequest: () -> Unit = {},
    confirmOnClick: () -> Unit = {},
    dismissOnClick: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            ButtonDialog(
                textButton = textConfirmButton,
                onClick = confirmOnClick
            )
        },
        dismissButton = {
            ButtonDialog(
                textButton = stringResource(R.string.cancel),
                onClick = dismissOnClick
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
                PasswordField(
                    text = passwordState.passwordText,
                    isError = passwordState.passwordIsError,
                    hint = stringResource(id = R.string.current_password),
                    errorText = passwordState.passwordErrorTextId?.let { id ->
                        stringResource(id = id)
                    },
                    onValueChange = onValueChange
                )
            }
        },
        containerColor = Color.White,
    )
}