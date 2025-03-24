package com.rdua.whiteboard.board.composable.utils

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.board.composable.utils.HandlePosition.MIDDLE
import com.rdua.whiteboard.board.constants.DefaultSizes
import com.rdua.whiteboard.board.model.LineModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Calculates the data for a line  [LineModel] based on the provided parameters, such as start or end coordinates, size,
 * and rotation angle, based on the provided item, offset, and handlePosition
 *
 * @param itemCoordinate The Line start coordinate value before resizing.
 * @param itemEndCoordinate The Line end coordinate value before resizing.
 * @param offset The offset for resizing.
 * @param handlePosition The position of the resizing handle.
 * @return The calculated line data, or
 * null if the object has reached its minimum size or if use of [MIDDLE] [handlePosition].
 * @throws IllegalArgumentException if use of [handlePosition] is not supported
 */
internal fun calculateLineData(
    itemCoordinate: DpOffset,
    itemEndCoordinate: DpOffset,
    offset: DpOffset,
    handlePosition: HandlePosition,
): LineData? {
    // Minimum handles touch interactive size:
    // half [start handle] size + [middle handle] size + half [end handle] size
    val minSize: Dp = DefaultSizes.minHandleInteractiveSize * 2

    val startCoordinate: DpOffset
    val endCoordinate: DpOffset
    val size: DpSize
    val rotationAngle: Float

    when (handlePosition) {

        HandlePosition.START -> {

            startCoordinate = itemCoordinate + offset
            endCoordinate = itemEndCoordinate
            size = calculateLineSize(startCoordinate, endCoordinate)

            if (size.width < minSize) return null

            rotationAngle = calculateRotationAngle(startCoordinate, endCoordinate)
        }

        HandlePosition.END -> {

            startCoordinate = itemCoordinate
            endCoordinate = itemEndCoordinate + offset
            size = calculateLineSize(startCoordinate, endCoordinate)

            if (size.width < minSize) return null

            rotationAngle = calculateRotationAngle(startCoordinate, endCoordinate)
        }

        MIDDLE -> return null

        else -> {
            throw IllegalArgumentException("This function doesn't support $handlePosition")
        }
    }

    return LineData(
        startCoordinate = startCoordinate,
        endCoordinate = endCoordinate,
        size = size,
        rotationAngle = rotationAngle,
    )
}

/**
 * Calculates the size of a line based on its start and end coordinates
 *
 * @param startCoordinate The starting coordinate.
 * @param endCoordinate The ending coordinate.
 * @return The size of the line.
 */
internal fun calculateLineSize(
    startCoordinate: DpOffset,
    endCoordinate: DpOffset,
): DpSize {
    val deltaX: Float = (endCoordinate.x - startCoordinate.x).value
    val deltaY: Float = (endCoordinate.y - startCoordinate.y).value

    //  Calculates the length of the line
    val lengthLine: Float = sqrt(deltaX * deltaX + deltaY * deltaY)

    return DpSize(width = lengthLine.dp, height = DefaultSizes.lineSize.height)
}

/**
 * Calculates the rotation angle between two points.
 *
 * @param startCoordinate The starting coordinate.
 * @param endCoordinate The ending coordinate.
 * @return The rotation angle in degrees.
 */
internal fun calculateRotationAngle(
    startCoordinate: DpOffset,
    endCoordinate: DpOffset,
): Float {
    val deltaX = (endCoordinate.x - startCoordinate.x).value
    val deltaY = (endCoordinate.y - startCoordinate.y).value

    // Calculates an angle in radians
    val angleRadian: Float = atan2(deltaY, deltaX)
    // Converts angle to degrees
    val angleDegrees: Float = angleRadian * (180 / Math.PI).toFloat()

    // Returns the angle in degrees, taking into account the negative value
    return if (angleDegrees < 0) angleDegrees + 360 else angleDegrees
}

/**
 * Calculates the offset taking into account the rotation of the element.
 *
 * @param rotationAngle The rotation angle in degrees.
 * @param offset The original offset.
 * @return The new offset taking into account the rotation.
 */
internal fun calculateOffsetWithRotation(
    rotationAngle: Double,
    offset: DpOffset,
): DpOffset {
    // Converts the rotation angle from degrees to radians
    val angleRad: Double = Math.toRadians(rotationAngle)

    // Gets the offset along the X and Y axis
    val offsetX: Float = offset.x.value
    val offsetY: Float = offset.y.value

    // Applies rotation to displacement
    val rotatedOffsetX: Double = offsetX * cos(angleRad) - offsetY * sin(angleRad)
    val rotatedOffsetY: Double = offsetX * sin(angleRad) + offsetY * cos(angleRad)

    // Returns the new offset
    return DpOffset(x = rotatedOffsetX.dp, y = rotatedOffsetY.dp)
}
