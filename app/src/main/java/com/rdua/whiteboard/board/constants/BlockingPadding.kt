package com.rdua.whiteboard.board.constants

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * This class is used to standardize the paddings in [BlockableBoardItem]
 * and calculate the item's position on the board.
 */
data class BlockingPadding(
    val top: Dp = 0.dp,
    val start: Dp = 0.dp,
    val end: Dp = 0.dp,
    val bottom: Dp = 0.dp,
) {
    constructor(all: Dp) : this(top = all, start = all, end = all, bottom = all)

    constructor(bottom: Dp, other: Dp) : this(
        bottom = bottom,
        top = other,
        start = other,
        end = other
    )
}
