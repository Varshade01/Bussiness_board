package com.rdua.whiteboard.board.draw_shape

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawBlockedCanvas() {
    drawRect(
        color = Color.Gray,
        alpha = 0.5f
    )

    drawRectangleBorder(isLocked = true)
}