package com.rdua.whiteboard.board.composable.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Calculates the scale change factor based on the difference between [currentSize] and [newSize].
 *
 * To preserve the aspect ratio after scale, the scale change will be the average of the difference
 * in width and height.
 *
 * @param newSize The size to scale to.
 * @param currentSize The current size before scaling.
 * @param initialSize The original size (the size of this item at x1 scale).
 * @return The scale change that is needed to apply to scale [currentSize] to [newSize] while preserving
 * the aspect ration.
 */
internal fun calculateScaleChange(
    newSize: Size,
    currentSize: Size,
    initialSize: Size
): Float {
    val scaleX = (newSize.width - currentSize.width) / initialSize.width
    val scaleY = (newSize.height - currentSize.height) / initialSize.height
    return (scaleX + scaleY) / 2
}

/**
 * Calculates the offset after scaling.
 *
 * This function assumes the origin (x0, y0) as top left point. It calculates the offset change
 * for this item after increasing its scale by the [scaleFactor] taking into account [handlePosition].
 *
 * By default scaling is set from [HandlePosition.BOTTOM_END]. That means that the height and the
 * width of the item will change, but the origin (x0, y0) will not move. To scale from [HandlePosition.BOTTOM_START]
 * as the scale reduces, the item's x coordinate for origin (x0, y0) has to increase, while y
 * coordinate will stay the same. So when scaling from [HandlePosition.BOTTOM_START] the item's
 * top right corner serves as the "origin point".
 *
 * @param scaleFactor The scale change that will be applied to current item's scale.
 * @param initialSize The original size (the size of this item at x1 scale).
 * @param handlePosition the placement of the drag handle that is dragged.
 * @return The offset change after scaling in a specified direction.
 */
internal fun calculateScaleOffset(
    scaleFactor: Float,
    initialSize: Size,
    handlePosition: HandlePosition = HandlePosition.BOTTOM_END,
): Offset {
    return when (handlePosition) {
        HandlePosition.TOP_START -> {
            Offset(
                x = initialSize.width.times(-scaleFactor),
                y = initialSize.height.times(-scaleFactor)
            )
        }

        HandlePosition.TOP_END -> {
            Offset(
                x = 0f,
                y = initialSize.height.times(-scaleFactor)
            )
        }

        HandlePosition.BOTTOM_START -> {
            Offset(
                x = initialSize.width.times(-scaleFactor),
                y = 0f
            )
        }

        HandlePosition.BOTTOM_END -> {
            Offset.Zero
        }

        else -> {
            throw IllegalArgumentException("This function doesn't support $handlePosition")
        }
    }
}

/**
 * Calculates the offset after scaling. Is designed to deal with the situation where the scaled content
 * can be smaller than its parent composable (container). This offset is relative to the container size
 * change, not the content size change.
 *
 * @param scaleChange The scale change that will be applied to current item's scale.
 * @param initialSize The original size (the size of this item at x1 scale).
 * @param handlePosition the placement of the drag handle that is dragged.
 * @return The offset change after scaling in a specified direction.
 */
internal fun calculateScaleOffset(
    scaleChange: Float,
    currentContentSize: Size,
    currentContainerSize: Size,
    minContainerSize: Size,
    initialSize: Size,
    handlePosition: HandlePosition = HandlePosition.BOTTOM_END,
): Offset {
    // Content size after after current scaling.
    val newContentSize = currentContentSize.copy(
        width = currentContentSize.width + (initialSize.width * scaleChange),
        height = currentContentSize.height + (initialSize.height * scaleChange),
    )

    // Compose usually rounds all sizes to IntSize (Modifier.size, Modifier.onSizeChanged).
    // This prevents weird jitters when scaling.
    val roundedMinContainerSize = minContainerSize.toIntSize().toSize()

    // The size of the container after scaling its content corrected with minimal constraint.
    val newContainerSize = Size(
        width = maxOf(roundedMinContainerSize.width, newContentSize.width),
        height = maxOf(roundedMinContainerSize.height, newContentSize.height),
    )

    // Scale difference between current and new container size.
    val scaleChangeX = (newContainerSize.width - currentContainerSize.width) / initialSize.width
    val scaleChangeY = (newContainerSize.height - currentContainerSize.height) / initialSize.height

    return when (handlePosition) {
        HandlePosition.TOP_START -> {
            Offset(
                x = initialSize.width.times(-scaleChangeX),
                y = initialSize.height.times(-scaleChangeY)
            )
        }

        HandlePosition.TOP_END -> {
            Offset(
                x = 0f,
                y = initialSize.height.times(-scaleChangeY)
            )
        }

        HandlePosition.BOTTOM_START -> {
            Offset(
                x = initialSize.width.times(-scaleChangeX),
                y = 0f
            )
        }

        HandlePosition.BOTTOM_END -> {
            Offset.Zero
        }

        else -> {
            throw IllegalArgumentException("This function doesn't support $handlePosition")
        }
    }
}

/**
 * Calculates the offset after resizing.
 *
 * When dealing with the situation where the scaled content can be smaller than its parent composable
 * (container), pass container's values to [currentSize] and [newSize].
 *
 * Rounding x to Int prevents minor positional shifts caused by using Float size and coordinates.
 *
 * @param currentSize The current size before resizing.
 * @param newSize the new size after resizing.
 * @param handlePosition the placement of the drag handle that is dragged.
 * @return The offset change after resizing in a specified direction.
 */
internal fun calculateResizeOffset(
    currentSize: Size,
    newSize: Size,
    handlePosition: HandlePosition,
): Offset {
    return when (handlePosition) {
        HandlePosition.TOP_START -> Offset(
            x = (currentSize.width - newSize.width).roundToInt().toFloat(),
            y = (currentSize.height - newSize.height).roundToInt().toFloat(),
        )

        HandlePosition.TOP_END -> Offset(
            x = 0f,
            y = (currentSize.height - newSize.height).roundToInt().toFloat(),
        )

        HandlePosition.MIDDLE_START,
        HandlePosition.BOTTOM_START -> Offset(
            x = (currentSize.width - newSize.width).roundToInt().toFloat(),
            y = 0f,
        )

        HandlePosition.MIDDLE_END,
        HandlePosition.BOTTOM_END -> Offset.Zero

        else -> {
            throw IllegalArgumentException("This function doesn't support $handlePosition")
        }
    }
}

/**
 * Calculates new size for when user uses scale handles.
 *
 * The new size value will depend on two factors:
 * 1. The position of the scale handle relatively to the UI element.
 * 2. The direction of drag.
 * (Ex. when the top left handle is dragged to the right and down, then offset.x > 0 and offset.y > 0.
 * But as user drags top left handle, we need to decrease the width and the height of the UI element.
 * We then apply offset to move it by the same offset for the bottom right point to not move.
 *
 * The resulting size is calculated given this two factors. The use of [initialSize] and [totalDragOffset]
 * allows to easily handle the situation when user drags cursor way past minimal width coordinate.
 * When that happens, the resizing handle freezes in place and doesn't follow user drag anymore.
 * The user needs to drag back all the way to the handle for the width to change again.
 *
 * @param minSize minimal new size.
 * @param initialSize the size of the UI element at the start of drag.
 * @param totalDragOffset total offset from the start drag point to current x,y coordinate.
 * @param handlePosition the placement of the drag handle that is dragged.
 * @return new size value after applying [totalDragOffset], but no less than [minSize].
 */
internal fun calculateNewSize(
    minSize: Size,
    initialSize: Size,
    totalDragOffset: Offset,
    handlePosition: HandlePosition,
): Size {
    return when (handlePosition) {
        HandlePosition.TOP_START -> {
            Size(
                width = maxOf(minSize.width, initialSize.width - totalDragOffset.x),
                height = maxOf(minSize.height, initialSize.height - totalDragOffset.y)
            )
        }

        HandlePosition.TOP_END -> {
            Size(
                width = maxOf(minSize.width, initialSize.width + totalDragOffset.x),
                height = maxOf(minSize.height, initialSize.height - totalDragOffset.y)
            )
        }

        HandlePosition.BOTTOM_START -> {
            Size(
                width = maxOf(minSize.width, initialSize.width - totalDragOffset.x),
                height = maxOf(minSize.height, initialSize.height + totalDragOffset.y)
            )
        }

        HandlePosition.BOTTOM_END -> {
            Size(
                width = maxOf(minSize.width, initialSize.width + totalDragOffset.x),
                height = maxOf(minSize.height, initialSize.height + totalDragOffset.y)
            )
        }

        else -> {
            throw IllegalArgumentException("This function doesn't support $handlePosition")
        }
    }
}

/**
 * Calculates new size for when user uses scale handles.
 *
 * @param minSize minimal new size.
 * @param maxSize maximum new size.
 * @param initialSize the size of the UI element at the start of drag.
 * @param totalDragOffset total offset from the start drag point to current x,y coordinate.
 * @param handlePosition the placement of the drag handle that is dragged.
 * @return new size value after applying [totalDragOffset], no smaller than [minSize] and no larger than [maxSize].
 */
internal fun calculateNewSize(
    minSize: Size,
    maxSize: Size,
    initialSize: Size,
    totalDragOffset: Offset,
    handlePosition: HandlePosition,
): Size {
    val newSize = calculateNewSize(
        minSize = minSize,
        initialSize = initialSize,
        totalDragOffset = totalDragOffset,
        handlePosition = handlePosition
    )
    return newSize.copy(
        width = minOf(maxSize.width, newSize.width),
        height = minOf(maxSize.height, newSize.height)
    )
}

/**
 * Calculates new width for when user drags resize handles.
 *
 * New width value will be a difference between [initialWidth] value (at the start of the resizing), and [totalDragOffset]
 * from the start dragging point.
 * The resulting new width will be no less than [minWidth].
 *
 * Using this approach instead of calculating the difference between the delta offset and current size
 * allows to easily handle the situation when user drags cursor way past minimal width coordinate.
 *
 * @param minWidth minimal new width value.
 * @param initialWidth the width of the UI element at the start of drag.
 * @param totalDragOffset total x offset from the start drag point to current x coordinate.
 * @param handlePosition the placement of the drag handle that is dragged.
 * @return new width value after applying [totalDragOffset], but no less than [minWidth].
 */
internal fun calculateResizedWidth(
    minWidth: Float,
    initialWidth: Float,
    totalDragOffset: Float,
    handlePosition: HandlePosition,
): Float {
    val newWidth = when (handlePosition) {
        HandlePosition.MIDDLE_START -> initialWidth - totalDragOffset
        HandlePosition.MIDDLE_END -> initialWidth + totalDragOffset

        else -> {
            throw IllegalArgumentException("This function doesn't support $handlePosition")
        }
    }

    return maxOf(minWidth, newWidth)
}

/**
 * Converts [Size] to [IntSize] rounding width and height values to [Int].
 */
fun Size.toIntSize(): IntSize = IntSize(
    width = width.roundToInt(),
    height = height.roundToInt(),
)

/**
 * Rounds [Size] width and height values to their integer parts.
 * @return this [Size] with both width and height rounded to integers.
 */
fun Size.roundValuesToInt(): Size = Size(
    width = width.roundToInt().toFloat(),
    height = height.roundToInt().toFloat(),
)


/**
 * Rounds [TextUnit] value to the specified [decimals] places.
 */
fun TextUnit.round(decimals: Int = 0): TextUnit {
    val multiplier = 10f.pow(decimals)
    return ((value * multiplier).roundToInt() / multiplier).sp
}

/**
 * Returns `true` if this [Size] dimensions are *bigger* than [other]'s dimensions.
 */
internal fun Size.isBiggerThan(other: Size) =
    width > other.width || height > other.height

/**
 * Returns `true` if this [Size] dimensions are *smaller* than [other]'s dimensions.
 */
internal fun Size.isSmallerThan(other: Size) =
    width < other.width && height < other.height
