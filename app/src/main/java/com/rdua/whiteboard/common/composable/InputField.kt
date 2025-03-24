package com.rdua.whiteboard.common.composable

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun InputField(
    text: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        label = {
            hint?.let { Text(text = hint) }
        },
        isError = isError,
        singleLine = true,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedLeadingIconColor = MaterialTheme.colors.textFieldAccent,
            unfocusedLeadingIconColor = MaterialTheme.colors.textFieldAccent,
            errorLeadingIconColor = MaterialTheme.colors.textFieldAccent,
            focusedTrailingIconColor = MaterialTheme.colors.textFieldAccent,
            unfocusedTrailingIconColor = MaterialTheme.colors.textFieldAccent,
            focusedIndicatorColor = MaterialTheme.colors.textFieldAccent,
            focusedLabelColor = MaterialTheme.colors.textFieldAccent
        ),
        supportingText = {
            if (errorText != null) Text(text = errorText)
        }
    )
}