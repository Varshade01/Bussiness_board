package com.rdua.whiteboard.board_item.screen

import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.model.ShapeType

sealed class ShapeItem(
    val iconId: Int,
    val titleId: Int,
    val type: ShapeType,
) {
    companion object {
        val shapeItemList = listOf(
            SquareShapeItem,
            CircleShapeItem,
            TriangleShapeItem,
            StarShapeItem,
            RoundedSquareShapeItem,
            RhombusShapeItem,
            ParallelogramShapeItem,
            PentagonShapeItem,
            HexagonShapeItem,
            LineShapeItem,
//            RightArrowShapeItem,
//            LeftArrowShapeItem
        )
    }

    object SquareShapeItem : ShapeItem(
        iconId = R.drawable.ic_square,
        titleId = R.string.rectangle_shape,
        type = ShapeType.RECTANGLE
    )

    object CircleShapeItem : ShapeItem(
        iconId = R.drawable.ic_circle,
        titleId = R.string.oval_shape,
        type = ShapeType.OVAL
    )

    object TriangleShapeItem : ShapeItem(
        iconId = R.drawable.ic_triangle,
        titleId = R.string.triangle_shape,
        type = ShapeType.TRIANGLE
    )

    object StarShapeItem : ShapeItem(
        iconId = R.drawable.ic_star,
        titleId = R.string.star_shape,
        type = ShapeType.STAR
    )

    object RoundedSquareShapeItem : ShapeItem(
        iconId = R.drawable.ic_rounded_square,
        titleId = R.string.rounded_rectangle_shape,
        type = ShapeType.ROUNDED_RECTANGLE
    )

    object RhombusShapeItem : ShapeItem(
        iconId = R.drawable.ic_rhombus,
        titleId = R.string.rhombus_shape,
        type = ShapeType.RHOMBUS
    )

    object ParallelogramShapeItem : ShapeItem(
        iconId = R.drawable.ic_parallelogram,
        titleId = R.string.parallelogram_shape,
        type = ShapeType.PARALLELOGRAM
    )

    object PentagonShapeItem : ShapeItem(
        iconId = R.drawable.ic_pentagon,
        titleId = R.string.pentagon_shape,
        type = ShapeType.PENTAGON
    )

    object HexagonShapeItem : ShapeItem(
        iconId = R.drawable.ic_hexagon,
        titleId = R.string.hexagon_shape,
        type = ShapeType.HEXAGON
    )

    object LineShapeItem : ShapeItem(
        iconId = R.drawable.ic_line,
        titleId = R.string.line_shape,
        type = ShapeType.LINE
    )

    object RightArrowShapeItem : ShapeItem(
        iconId = R.drawable.ic_right_arrow,
        titleId = R.string.right_arrow_shape,
        type = ShapeType.RIGHT_ARROW
    )

    object LeftArrowShapeItem : ShapeItem(
        iconId = R.drawable.ic_left_arrow,
        titleId = R.string.left_arrow_shape,
        type = ShapeType.LEFT_ARROW
    )
}
