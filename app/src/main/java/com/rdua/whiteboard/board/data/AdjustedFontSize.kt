package com.rdua.whiteboard.board.data

import androidx.compose.ui.unit.TextUnit

data class AdjustedFontSize(
    val currentSize: TextUnit,
    val maxSize: TextUnit,
    val autoAdjustMode: Boolean
)