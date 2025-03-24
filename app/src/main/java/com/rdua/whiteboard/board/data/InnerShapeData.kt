package com.rdua.whiteboard.board.data

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize

/**
 * Contains [size] and [offset] of the inner area of a Shape. These values should be calculated in
 * DrawScope for each Shape and passed to the Composable function to set correct size and position
 * of inner Text area.
 */
data class InnerShapeData(val size: DpSize, val offset: DpOffset = DpOffset.Zero)