package com.rdua.whiteboard.board_item.screen.toolbar.text_bar

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

data class TextStyleState(
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderlined: Boolean = false,
    val isLineThrough: Boolean = false
)

/**
 * Converts [TextStyle] to [TextStyleState].
 */
internal fun TextStyle.toTextStyleState(): TextStyleState = TextStyleState(
    isBold = fontWeight == FontWeight.Bold,
    isItalic = fontStyle == FontStyle.Italic,
    isUnderlined = textDecoration?.contains(TextDecoration.Underline) ?: false,
    isLineThrough = textDecoration?.contains(TextDecoration.LineThrough) ?: false,
)

/**
 * Converts [TextStyleState] to [TextStyle] containing information on [TextStyle.fontWeight],
 * [TextStyle.fontStyle] and [TextStyle.textDecoration]. Other fields have default values.
 */
internal fun TextStyleState.toTextStyle(): TextStyle = TextStyle(
    fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
    fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
    textDecoration = createTextDecoration(underline = isUnderlined, lineThrough = isLineThrough),
)

/**
 * Creates [TextDecoration] with [TextDecoration.Underline] and [TextDecoration.LineThrough] stylings.
 */
private fun createTextDecoration(underline: Boolean?, lineThrough: Boolean?): TextDecoration? {
    return if (underline == true && lineThrough == true) {
        TextDecoration.Underline + TextDecoration.LineThrough
    } else if (underline == true) {
        TextDecoration.Underline
    } else if (lineThrough == true) {
        TextDecoration.LineThrough
    } else null
}