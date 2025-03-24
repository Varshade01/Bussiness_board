package com.rdua.whiteboard.board.composable.handles

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

/**
 * Provides an abstraction of Handle UI component.
 *
 * Handle should be drawn inside a Box using [drawShape]. [clipShape] is than used in Modifier to
 * clip the Box to a certain [Shape].
 */
interface HandleShape {
    val size: DpSize
    val borderWidth: Dp
    val color: Color
    val borderColor: Color
    val clipShape: Shape
    fun drawShape(): DrawScope.() -> Unit
}

class SquareHandleShape(
    override val size: DpSize = DpSize.Zero,
    override val borderWidth: Dp = 1.dp,
    override val color: Color = Color.Transparent,
    override val borderColor: Color = Color.Black,
    override val clipShape: Shape = RectangleShape,
) : HandleShape {

    override fun drawShape(): DrawScope.() -> Unit = {
        // Handle size can be passed as a parameter or can occupy the size of the canvas.
        val handleSize = if (this@SquareHandleShape.size != DpSize.Zero) {
            this@SquareHandleShape.size.toSize()
        } else {
            size
        }
        // Inner offset for drawn handle shape to CENTER it in its container.
        val offset = if (handleSize == size) {
            Offset.Zero
        } else {
            Offset(
                x = (size.width - handleSize.width) / 2,
                y = (size.height - handleSize.height) / 2,
            )
        }

        // Main shape.
        drawRect(
            color = color,
            topLeft = offset,
            size = handleSize,
        )

        // Border shape.
        drawRect(
            color = borderColor,
            topLeft = offset,
            size = handleSize,
            style = Stroke(
                width = borderWidth.toPx(),
            ),
        )
    }
}

data class CircleHandleShape(
    override val size: DpSize = DpSize.Zero,
    override val borderWidth: Dp = 1.dp,
    override val color: Color = Color.Transparent,
    override val borderColor: Color = Color.Black,
    override val clipShape: Shape = CircleShape,
) : HandleShape {
    override fun drawShape(): DrawScope.() -> Unit = {
        // Handle size can be passed as a parameter or can occupy the size of the canvas.
        val handleSize = if (this@CircleHandleShape.size != DpSize.Zero) {
            this@CircleHandleShape.size.toSize()
        } else {
            size
        }

        drawCircle(
            color = color,
            radius = handleSize.width / 2
        )

        drawCircle(
            color = borderColor,
            radius = handleSize.width / 2,
            style = Stroke(
                width = borderWidth.toPx(),
            ),
        )
    }
}