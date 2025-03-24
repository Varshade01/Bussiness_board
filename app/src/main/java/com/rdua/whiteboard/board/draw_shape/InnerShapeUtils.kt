package com.rdua.whiteboard.board.draw_shape

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.board.data.InnerShapeData
import com.rdua.whiteboard.board.model.ShapeModel
import com.rdua.whiteboard.board.model.ShapeType
import kotlin.math.sqrt

/**
 * Returns [InnerShapeData] containing the size and the offset for the text area inside a Shape.
 */
internal fun calculateInnerShapeData(
    model: ShapeModel,
    horizontalPaddings: Dp = 0.dp,
    verticalPaddings: Dp = 0.dp,
): InnerShapeData = when (model.type) {
    ShapeType.RECTANGLE -> getDefaultInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
    ShapeType.OVAL -> getOvalInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
    ShapeType.ROUNDED_RECTANGLE -> getDefaultInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
    ShapeType.LINE -> getDefaultInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
    ShapeType.TRIANGLE -> getTriangleInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
    ShapeType.STAR -> getStarInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
    ShapeType.RHOMBUS -> getRhombusInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
    ShapeType.PARALLELOGRAM -> getParallelogramInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
    ShapeType.PENTAGON -> getPentagonInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
    ShapeType.HEXAGON -> getHexagonInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
    ShapeType.RIGHT_ARROW -> getDefaultInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
    ShapeType.LEFT_ARROW -> getDefaultInnerShapeData(model.size, horizontalPaddings, verticalPaddings)
}

internal fun calculateInnerShapeData(
    model: ShapeModel,
    paddings: Dp = 0.dp,
): InnerShapeData = calculateInnerShapeData(
    model = model,
    horizontalPaddings = paddings,
    verticalPaddings = paddings,
)

// For many shapes the inside text area size matches the Shape's size and no offset needed.
private fun getDefaultInnerShapeData(
    size: DpSize,
    horizontalPaddings: Dp,
    verticalPaddings: Dp
): InnerShapeData {
    return InnerShapeData(
        size = toDpSizeWithPadding(
            size = size,
            horizontal = horizontalPaddings,
            vertical = verticalPaddings,
        ),
    )
}

private fun getOvalInnerShapeData(
    size: DpSize,
    horizontalPaddings: Dp,
    verticalPaddings: Dp
): InnerShapeData {
    // The formula for inner area of an ellipse (Hr, Vr -> horizontal/vertical radii):
    // W = Hr * sqrt(2)
    // H = Vr * sqrt(2)
    val innerWidth = (size.width / 2) * sqrt(2f)
    val innerHeight = (size.height / 2) * sqrt(2f)

    return InnerShapeData(
        size = toDpSizeWithPadding(
            size = DpSize(width = innerWidth, height = innerHeight),
            horizontal = horizontalPaddings,
            vertical = verticalPaddings
        ),
    )
}

private fun getTriangleInnerShapeData(
    size: DpSize,
    horizontalPaddings: Dp,
    verticalPaddings: Dp
): InnerShapeData {
    return InnerShapeData(
        size = toDpSizeWithPadding(
            size = DpSize(width = size.width / 2, height = size.height / 2),
            horizontal = horizontalPaddings,
            vertical = verticalPaddings,
        ),
    )
}

private fun getStarInnerShapeData(
    size: DpSize,
    horizontalPaddings: Dp,
    verticalPaddings: Dp
): InnerShapeData {
    // Values are approximated based on current star draw Path in PathShapes.
    return InnerShapeData(
        size = toDpSizeWithPadding(
            size = DpSize(
                width = size.width * 0.42f,
                height = size.height * 0.45f
            ),
            horizontal = horizontalPaddings,
            vertical = verticalPaddings,
        ),
        offset = DpOffset(x = 0.dp, y = size.height * 0.021f)
    )
}

private fun getRhombusInnerShapeData(
    size: DpSize,
    horizontalPaddings: Dp,
    verticalPaddings: Dp
): InnerShapeData {
    return InnerShapeData(
        size = toDpSizeWithPadding(
            size = DpSize(width = size.width / 2, height = size.height / 2),
            horizontal = horizontalPaddings,
            vertical = verticalPaddings,
        ),
    )
}

private fun getParallelogramInnerShapeData(
    size: DpSize,
    horizontalPaddings: Dp,
    verticalPaddings: Dp
): InnerShapeData {
    // Values are approximated based on current parallelogram draw Path in PathShapes.
    return InnerShapeData(
        size = toDpSizeWithPadding(
            size = DpSize(
                width = size.width * 0.784f,
                height = size.height
            ),
            horizontal = horizontalPaddings,
            vertical = verticalPaddings,
        ),
    )
}

private fun getPentagonInnerShapeData(
    size: DpSize,
    horizontalPaddings: Dp,
    verticalPaddings: Dp
): InnerShapeData {
    // Values are approximated based on current pentagon draw Path in PathShapes.
    return InnerShapeData(
        size = toDpSizeWithPadding(
            size = DpSize(
                width = size.width * 0.67f,
                height = size.height * 0.75f
            ),
            horizontal = horizontalPaddings,
            vertical = verticalPaddings,
        ),
    )
}

private fun getHexagonInnerShapeData(
    size: DpSize,
    horizontalPaddings: Dp,
    verticalPaddings: Dp
): InnerShapeData {
    // Values are approximated based on current hexagon draw Path in PathShapes.
    return InnerShapeData(
        size = toDpSizeWithPadding(
            size = DpSize(
                width = size.width * 0.72f,
                height = size.height
            ),
            horizontal = horizontalPaddings,
            vertical = verticalPaddings,
        ),
    )
}

/**
 * Retracts [horizontal] and [vertical] paddings from a given [size].
 */
private fun toDpSizeWithPadding(
    size: DpSize,
    vertical: Dp = 0.dp,
    horizontal: Dp = 0.dp,
): DpSize = DpSize(
    width = size.width - vertical * 2,
    height = size.height - horizontal * 2
)