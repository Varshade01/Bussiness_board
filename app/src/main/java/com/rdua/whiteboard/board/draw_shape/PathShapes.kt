package com.rdua.whiteboard.board.draw_shape

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp

internal fun DrawScope.getTrianglePath(): Path = Path().apply {
    with(size) {
        moveTo(x = width * 0.5f, y = 2.dp.toPx())
        lineTo(x = width, y = height)
        lineTo(x = 0f, y = height)
        close()
    }
}

internal fun DrawScope.getStarPath(): Path = Path().apply {
    with(size) {
        moveTo(x = 2.dp.toPx(), y = height * 0.3f)
        lineTo(x = width * 0.38f, y = height * 0.3f)
        lineTo(x = width * 0.5f, y = 2.dp.toPx())
        lineTo(x = width * 0.62f, y = height * 0.3f)
        lineTo(x = width - 2.dp.toPx(), y = height * 0.3f)
        lineTo(x = width * 0.708f, y = height * 0.575f)
        lineTo(x = width * 0.835f, y = height - 2.dp.toPx())
        lineTo(x = width * 0.5f, y = height * 0.744f)
        lineTo(x = width * 0.165f, y = height - 2.dp.toPx())
        lineTo(x = width * 0.292f, y = height * 0.575f)
        close()
    }
}

internal fun DrawScope.getRhombusPath(): Path = Path().apply {
    with(size) {
        moveTo(x = width * 0.5f, y = 2.dp.toPx())
        lineTo(x = width - 2.dp.toPx(), y = height * 0.5f)
        lineTo(x = width * 0.5f, y = height - 2.dp.toPx())
        lineTo(x = 2.dp.toPx(), y = height * 0.5f)
        close()
    }
}

internal fun DrawScope.getParallelogramPath(): Path = Path().apply {
    with(size) {
        moveTo(x = width * 0.108f, y = 0f)
        lineTo(x = width, y = 0f)
        lineTo(x = width * 0.892f, y = height)
        lineTo(x = 0f, y = height)
        close()
    }
}

internal fun DrawScope.getPentagonPath(): Path = Path().apply {
    with(size) {
        moveTo(x = width * 0.5f, y = 2.dp.toPx())
        lineTo(x = width - 2.dp.toPx(), y = height * 0.383f)
        lineTo(x = width * 0.835f, y = height)
        lineTo(x = width * 0.165f, y = height)
        lineTo(x = 2.dp.toPx(), y = height * 0.383f)
        close()
    }
}

internal fun DrawScope.getHexagonPath(): Path = Path().apply {
    with(size) {
        moveTo(x = width * 0.144f, y = 0f)
        lineTo(x = width * 0.856f, y = 0f)
        lineTo(x = width - 2.dp.toPx(), y = height * 0.5f)
        lineTo(x = width * 0.856f, y = height)
        lineTo(x = width * 0.144f, y = height)
        lineTo(x = 2.dp.toPx(), y = height * 0.5f)
        close()
    }
}

internal fun DrawScope.getRightArrowPath(): Path = Path().apply {
    with(size) {
        moveTo(x = 0f, y = height * 0.5f)
        lineTo(x = width, y = height * 0.5f)

        moveTo(x = width * 0.9f, y = height * 0.43f)
        lineTo(x = width, y = height * 0.5f)
        lineTo(x = width * 0.9f, y = height * 0.57f)
    }
}

internal fun DrawScope.getLeftArrowPath(): Path = Path().apply {
    with(size) {
        moveTo(x = width, y = height * 0.5f)
        lineTo(x = 0f, y = height * 0.5f)

        moveTo(x = width * 0.1f, y = height * 0.43f)
        lineTo(x = 0f, y = height * 0.5f)
        lineTo(x = width * 0.1f, y = height * 0.57f)
    }
}
