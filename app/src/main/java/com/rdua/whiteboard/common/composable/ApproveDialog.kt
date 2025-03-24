package com.rdua.whiteboard.common.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rdua.whiteboard.R
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun ApproveDialog(
    titleDialog: String,
    textDialog: String,
    textConfirmButton: String = stringResource(R.string.yes),
    textDismissButton: String = stringResource(R.string.cancel),
    onDismissRequest: () -> Unit = { },
    confirmOnClick: () -> Unit = { },
    dismissOnClick: () -> Unit = { },
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
                textButton = textDismissButton,
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
            Text(
                text = textDialog,
                style = MaterialTheme.typography.titleMedium
            )
        },
        containerColor = Color.White,
    )
}

@Preview
@Composable
fun ApproveDialogPreview() {
    ApproveDialog(
        titleDialog = "Logout",
        textDialog = "Do you want to logout?",
        textConfirmButton = "Logout",
    )
}
