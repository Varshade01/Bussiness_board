package com.rdua.whiteboard.board.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import com.rdua.whiteboard.board.utils.consumeAllPointerEvents
import com.rdua.whiteboard.board.utils.onKeyboardDismiss

/**
 * Extends SimpleTextField functionality by handling focus. Passes focus to the text field as soon
 * as user starts editing Board item's text.
 *
 * @param text the input [String] to be shown in the text field.
 * @param modifier optional [Modifier] for this text field.
 * @param textStyle Style configuration that applies at character level such as color, font etc.
 * @param onTextChange the callback that is triggered when the input service updates the text. An
 * updated text comes as a parameter of the callback.
 * @param onFinishEditing the callback that is triggered when the IME keyboard has been closed.
 */
@Composable
fun BoardTextField(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    onTextChange: (String) -> Unit = { },
    onFinishEditing: () -> Unit = { }
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    SimpleTextField(
        modifier = modifier
            // Consuming pointer input lets this SimpleTextField handle clicks and taps.
            // (Prevents dragging, when trying to select the text etc.).
            .consumeAllPointerEvents()
            .focusRequester(focusRequester)
            .onKeyboardDismiss {
                onFinishEditing()
                focusManager.clearFocus()
            },
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        textStyle = textStyle
    )
}