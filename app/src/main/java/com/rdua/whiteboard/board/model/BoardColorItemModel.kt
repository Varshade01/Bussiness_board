package com.rdua.whiteboard.board.model

import androidx.compose.ui.graphics.Color

sealed interface BoardColorItemModel : BoardItemModel {
    val color: Color
}
