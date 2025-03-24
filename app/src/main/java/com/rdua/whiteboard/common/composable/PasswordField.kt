package com.rdua.whiteboard.common.composable

import com.rdua.whiteboard.R
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordField(
    text: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    isError: Boolean = false,
    errorText: String? = null,
    passwordVisibleDescription: String = "",
    passwordInvisibleDescription: String = "",
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit = {},
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    InputField(
        modifier = modifier,
        text = text,
        hint = hint,
        isError = isError,
        errorText = errorText,
        onValueChange = onValueChange,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        leadingIcon = leadingIcon,
        trailingIcon = {
            val image = if (passwordVisible) {
                ImageVector.vectorResource(R.drawable.ic_visibility)
            } else {
                ImageVector.vectorResource(R.drawable.ic_visibility_off)
            }

            val description = if (passwordVisible) {
                passwordVisibleDescription
            } else {
                passwordInvisibleDescription
            }

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = description)
            }
        }
    )
}