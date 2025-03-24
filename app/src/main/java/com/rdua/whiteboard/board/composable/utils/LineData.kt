package com.rdua.whiteboard.board.composable.utils

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize


/**
 * Utility class for generalizing the properties of calculated lines
 */
data class LineData(
    val startCoordinate: DpOffset,
    val endCoordinate: DpOffset,
    val size: DpSize,
    val rotationAngle: Float,
)
