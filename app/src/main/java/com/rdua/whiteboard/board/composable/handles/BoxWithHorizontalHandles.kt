package com.rdua.whiteboard.board.composable.handles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize


@Composable
fun BoxWithHorizontalHandles(
    modifier: Modifier = Modifier,
    startCoordinate: DpOffset = DpOffset.Zero,
    endCoordinate: DpOffset = DpOffset.Zero,
    enabled: Boolean = false,
    resetKey: Any = Unit,
    handleShape: CircleHandleShape = CircleHandleShape(),
    onStartHandle: (offset: Offset) -> Unit = { },
    onMiddleHandle: (offset: Offset) -> Unit = { },
    onEndHandle: (offset: Offset) -> Unit = { },
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    drawContentObject: DrawScope.() -> Unit = { },
    content: @Composable BoxScope.() -> Unit = { },
) {

    val halfHandleSize: DpSize = handleShape.size / 2

    val startHandle = HandleData(
        offset = DpOffset(
            x = startCoordinate.x - halfHandleSize.width,
            y = startCoordinate.y - halfHandleSize.height
        ),
        onDrag = onStartHandle
    )
    val middleHandle = HandleData(
        offset = DpOffset(
            x = (startCoordinate.x + endCoordinate.x) / 2 - halfHandleSize.width,
            y = (startCoordinate.y + endCoordinate.y) / 2 - halfHandleSize.height
        ),
        onDrag = onMiddleHandle
    )
    val endHandle = HandleData(
        offset = DpOffset(
            x = endCoordinate.x - halfHandleSize.width,
            y = endCoordinate.y - halfHandleSize.height
        ),
        onDrag = onEndHandle
    )
    val middleHandleShape: CircleHandleShape = handleShape.copy(
        color = handleShape.borderColor,
        borderColor = handleShape.color,
    )

    Box(
        modifier = modifier
            .drawBehind {
                drawContentObject()
            },
    ) {
        content()

        if (enabled) {
            listOf(
                startHandle, middleHandle, endHandle,
            ).forEach { handle ->
                Handle(
                    resetKey = resetKey,
                    handleShape = if (handle == middleHandle) middleHandleShape else handleShape,
                    minSize = handleShape.size,
                    offset = handle.offset,
                    onDrag = handle.onDrag,
                    onDragStart = onDragStart,
                    onDragEnd = onDragEnd,
                )
            }
        }
    }
}
