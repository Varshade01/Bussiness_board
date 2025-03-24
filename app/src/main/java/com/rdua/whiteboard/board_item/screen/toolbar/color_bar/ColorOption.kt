package com.rdua.whiteboard.board_item.screen.toolbar.color_bar

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

sealed class ColorOption(val shape: Shape) {

    object BackgroundColor : ColorOption(shape = CircleShape)
    object BorderColor : ColorOption(shape = CircleShape)
    object FontColor : ColorOption(shape = RoundedCornerShape(6.dp))
}
