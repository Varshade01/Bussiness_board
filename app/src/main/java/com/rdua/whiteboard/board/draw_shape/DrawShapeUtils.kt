package com.rdua.whiteboard.board.draw_shape

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.board.model.ShapeModel
import com.rdua.whiteboard.board.model.ShapeType


fun DrawScope.onDrawShape(model: ShapeModel) = with(model) {
    when (type) {
        ShapeType.RECTANGLE -> drawRectShape(model = model)
        ShapeType.OVAL -> drawOvalShape(model = model)
        ShapeType.ROUNDED_RECTANGLE -> drawRoundRectShape(model = model)
        ShapeType.TRIANGLE -> drawPathShape(model = model, path = getTrianglePath())
        ShapeType.STAR -> drawPathShape(model = model, path = getStarPath())
        ShapeType.RHOMBUS -> drawPathShape(model = model, path = getRhombusPath())
        ShapeType.PARALLELOGRAM -> drawPathShape(model = model, path = getParallelogramPath())
        ShapeType.PENTAGON -> drawPathShape(model = model, path = getPentagonPath())
        ShapeType.HEXAGON -> drawPathShape(model = model, path = getHexagonPath())
        ShapeType.RIGHT_ARROW -> drawPathArrow(color, getRightArrowPath())
        ShapeType.LEFT_ARROW -> drawPathArrow(color, getLeftArrowPath())
        ShapeType.LINE -> { /* do nothing */ }
    }
}

private fun DrawScope.drawPathShape(model: ShapeModel, path: Path) {
    drawPath(
        path = path,
        color = model.color,
    )
    drawPath(
        path = path,
        color = model.borderColor,
        style = Stroke(
            width = 2.dp.toPx(),
        )
    )
}

private fun DrawScope.drawPathArrow(color: Color, path: Path) {
    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = 1.dp.toPx(),
        )
    )
}

private fun DrawScope.drawRectShape(model: ShapeModel) {
    drawRect(
        color = model.color
    )
    drawRect(
        color = model.borderColor,
        style = Stroke(
            width = 2.dp.toPx(),
        )
    )
}

private fun DrawScope.drawOvalShape(model: ShapeModel) {
    drawOval(
        color = model.color,
        size = size,
    )
    drawOval(
        color = model.borderColor,
        size = size,
        style = Stroke(
            width = 2.dp.toPx(),
        )
    )
}

private fun DrawScope.drawRoundRectShape(model: ShapeModel) {
    val roundRadius = 20f
    drawRoundRect(
        color = model.color,
        cornerRadius = CornerRadius(roundRadius, roundRadius)
    )
    drawRoundRect(
        color = model.borderColor,
        cornerRadius = CornerRadius(roundRadius, roundRadius),
        style = Stroke(
            width = 2.dp.toPx(),
        )
    )
}

internal fun DrawScope.drawLineShape(
    color: Color,
    lineWidth: Dp,
    lineStartCoordinate: DpOffset,
    lineEndCoordinate: DpOffset,
) {
    drawLine(
        color = color,
        strokeWidth = lineWidth.toPx(),
        start = Offset(x = lineStartCoordinate.x.toPx(), y = lineStartCoordinate.y.toPx()),
        end = Offset(x = lineEndCoordinate.x.toPx(), y = lineEndCoordinate.y.toPx())
    )
}
