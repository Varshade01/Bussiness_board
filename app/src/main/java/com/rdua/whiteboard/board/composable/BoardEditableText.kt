package com.rdua.whiteboard.board.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun BoardEditableText(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    placeholderTextStyle: TextStyle = textStyle,
    placeholder: String? = null,
    isEditMode: Boolean = false,
    onTextChange: (String) -> Unit = { },
    onFinishEditing: () -> Unit = { }
) {
    if (isEditMode) {
        BoardTextField(
            modifier = modifier,
            text = text,
            textStyle = textStyle,
            onTextChange = {
                onTextChange(it)
            },
            onFinishEditing = onFinishEditing
        )
    } else {
        Text(
            modifier = modifier,
            text = if (text.trimEnd().isNotEmpty()) text else placeholder ?: "",
            style = if (text.trimEnd().isEmpty() && placeholder != null) {
                placeholderTextStyle
            } else {
                textStyle
            }
        )
    }
}