package com.rdua.whiteboard.common.composable

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun ButtonDialog(
    textButton: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colors.primaryVariant
        )
    ) {
        Text(
            text = textButton,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Preview
@Composable
fun ButtonDialogPreview() {
    ButtonDialog(
        textButton = "Logout",
        onClick = { }
    )
}
