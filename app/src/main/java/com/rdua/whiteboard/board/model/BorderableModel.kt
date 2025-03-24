package com.rdua.whiteboard.board.model

import androidx.compose.ui.graphics.Color

/**
 * The [BorderableModel] interface specifies that the board item should have a colored border.
 */
sealed interface BorderableModel {
    val borderColor: Color
}
