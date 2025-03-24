package com.rdua.whiteboard.board.model

import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

sealed interface BoardTextItemModel : BoardItemModel {
    val text: String
    val textStyle: TextStyle
    val verticalTextAlignment: Alignment?
    val maxFontSize: TextUnit
}
