package com.rdua.whiteboard.board.composable.handles

import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset

internal data class HandleData(
    val offset: DpOffset = DpOffset.Zero,
    val alignment: Alignment = Alignment.TopStart,
    val onDrag: (Offset) -> Unit = { },
)