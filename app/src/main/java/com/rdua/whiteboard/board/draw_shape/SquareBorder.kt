package com.rdua.whiteboard.board.draw_shape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.ui.theme.Blue50

/**
 * Use this function to draw a selection border for items on the board for which
 * the resize function has not yet been implemented.
 */
@Deprecated(message = "For interactive corners use Boxes with Handles")
fun DrawScope.drawBorder(isLocked: Boolean) {
    drawRect(
        color = if (isLocked) Color.Gray else Blue50,
        size = size,
        style = Stroke(
            width = 2.dp.toPx()
        )
    )

    val sideCornerSquare = 6.dp.toPx()
    val sizeCornerSquare = Size(sideCornerSquare, sideCornerSquare)
    val halfSideSquare = sideCornerSquare / 2

    if (!isLocked) {
        listOf(
            Offset(x = 0 - halfSideSquare, y = 0 - halfSideSquare),
            Offset(x = size.width - halfSideSquare, y = 0 - halfSideSquare),
            Offset(x = size.width - halfSideSquare, y = size.height - halfSideSquare),
            Offset(x = 0 - halfSideSquare, y = size.height - halfSideSquare)
        ).forEach {
            drawCornerSquare(sizeCornerSquare = sizeCornerSquare, coordinate = it)
        }
    }
}

private fun DrawScope.drawCornerSquare(
    sizeCornerSquare: Size,
    coordinate: Offset,
) {
    drawRect(
        color = Color.White,
        topLeft = coordinate,
        size = sizeCornerSquare,
    )
    drawRect(
        color = Blue50,
        topLeft = coordinate,
        size = sizeCornerSquare,
        style = Stroke(
            width = 1.dp.toPx(),
        ),
    )
}

fun DrawScope.drawRectangleBorder(isLocked: Boolean) {
    drawRect(
        color = if (isLocked) Color.Gray else Blue50,
        size = size,
        style = Stroke(
            width = 2.dp.toPx()
        )
    )
}

// Unlike regular drawRect, draws border outside composable, regardless of stroke width.
fun DrawScope.drawNonOverlapRectangleBorder(isLocked: Boolean) {
    val strokeWidth = 2.dp.toPx()
    val strokeHalfWidth = strokeWidth / 2
    drawRect(
        topLeft = Offset(-strokeHalfWidth, -strokeHalfWidth),
        color = if (isLocked) Color.Gray else Blue50,
        size = size.copy(
            width = size.width + strokeWidth,
            height = size.height + strokeWidth
        ),
        style = Stroke(
            width = strokeWidth
        )
    )
}