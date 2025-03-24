package com.rdua.whiteboard.board.manager.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.board.model.FrameModel
import com.rdua.whiteboard.board.model.LineModel

/**
 * Calculates frame size.
 */
internal fun calculateFrameSize(item: BoardItemModel): DpSize {
    // Minimum free space around item as percentage
    val innerPadding = 0.36f
    // Default frame side size
    val defaultFrameSize = 256.dp

    // Item size taking into account inner padding
    val calculatedSize: DpSize = item.size + (item.size * innerPadding)
    // Larger of size value (calculatedSize)
    val maxCalculatedSize: Dp = calculatedSize.width.coerceAtLeast(calculatedSize.height)

    return if (maxCalculatedSize > defaultFrameSize)
        DpSize(width = maxCalculatedSize, height = maxCalculatedSize)
    else
        DpSize(width = defaultFrameSize, height = defaultFrameSize)
}

/**
 * Calculates frame offset relative to inner item
 */
internal fun calculateFrameCoordinate(
    item: BoardItemModel,
    frameSize: DpSize,
): DpOffset {
    val centreItemX: Dp
    val centreItemY: Dp

    if (item is LineModel) {
        centreItemX = (item.coordinate.x + item.endCoordinate.x) / 2
        centreItemY = (item.coordinate.y + item.endCoordinate.y) / 2
    } else {
        centreItemX = item.coordinate.x + item.blockingPadding.start + (item.size.width / 2)
        centreItemY = item.coordinate.y + item.blockingPadding.top + (item.size.height / 2)
    }

    val frameCoordinateX: Dp = centreItemX - (frameSize.width / 2)
    val frameCoordinateY: Dp = centreItemY - (frameSize.height / 2)

    return DpOffset(x = frameCoordinateX, y = frameCoordinateY)
}

/**
 * Checking if the inner item is outside the outer frame bound.
 */
internal fun isItemOutsideFrame(
    item: BoardItemModel,
    outerFrame: FrameModel?,
): Boolean {
    if (outerFrame == null) return false

    val leftFrameBound: Dp = outerFrame.coordinate.x
    val topFrameBound: Dp = outerFrame.coordinate.y
    val rightFrameBound: Dp = outerFrame.size.width
    val bottomFrameBound: Dp = outerFrame.size.height

    val leftItemBound: Dp = item.coordinate.x + item.blockingPadding.start
    val topItemBound: Dp = item.coordinate.y + item.blockingPadding.top
    val rightItemBound: Dp
    val bottomItemBound: Dp

    if (item is LineModel) {
        rightItemBound = item.endCoordinate.x + item.blockingPadding.end
        bottomItemBound = item.endCoordinate.y + item.blockingPadding.bottom
    } else {
        rightItemBound = item.coordinate.x + item.size.width + item.blockingPadding.start
        bottomItemBound = item.coordinate.y + item.size.height + item.blockingPadding.top
    }

    return leftItemBound < leftFrameBound
            || topItemBound < topFrameBound
            || rightItemBound > rightFrameBound
            || bottomItemBound > bottomFrameBound
}

/**
 * Checks if item is within in frame bound.
 */
internal fun checkItemWithinBounds(
    item: BoardItemModel,
    frame: FrameModel,
): Boolean {
    val leftFrameBound: Dp = frame.coordinate.x
    val topFrameBound: Dp = frame.coordinate.y
    val rightFrameBound: Dp = frame.coordinate.x + frame.size.width
    val bottomFrameBound: Dp = frame.coordinate.y + frame.size.height

    val leftItemBound: Dp = item.coordinate.x + item.blockingPadding.start
    val topItemBound: Dp = item.coordinate.y + item.blockingPadding.top
    val rightItemBound: Dp
    val bottomItemBound: Dp

    if (item is LineModel) {
        rightItemBound = item.endCoordinate.x + item.blockingPadding.end
        bottomItemBound = item.endCoordinate.y + item.blockingPadding.bottom
    } else {
        rightItemBound = item.coordinate.x + item.size.width + item.blockingPadding.start
        bottomItemBound = item.coordinate.y + item.size.height + item.blockingPadding.top
    }

    return leftItemBound > leftFrameBound
            && topItemBound > topFrameBound
            && rightItemBound < rightFrameBound
            && bottomItemBound < bottomFrameBound
}
