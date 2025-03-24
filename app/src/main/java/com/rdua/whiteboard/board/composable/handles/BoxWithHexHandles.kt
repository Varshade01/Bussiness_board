package com.rdua.whiteboard.board.composable.handles

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

@Composable
fun BoxWithHexHandles(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    middleResetKey: Any = Unit,
    cornerResetKey: Any = Unit,
    handleOffset: DpOffset = DpOffset.Zero,
    handleShape: HandleShape = SquareHandleShape(),
    contentAlignment: Alignment = Alignment.TopStart,
    minHandleInteractiveSize: DpSize = DpSize(40.dp, 40.dp),
    onTopStartDrag: (Offset) -> Unit = { },
    onTopEndDrag: (Offset) -> Unit = { },
    onBottomStartDrag: (Offset) -> Unit = { },
    onBottomEndDrag: (Offset) -> Unit = { },
    onMiddleStartDrag: (Offset) -> Unit = { },
    onMiddleEndDrag: (Offset) -> Unit = { },
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    content: @Composable () -> Unit = { },
) {
    // The actual handle size that should not be smaller than minimal interactive handle size.
    // Handle is placed inside a transparent Box that is [handleSizeInternal] in size even if visually
    // its smaller.
    val handleSizeInternal = remember {
        DpSize(
            width = max(minHandleInteractiveSize.width, handleShape.size.width),
            height = max(minHandleInteractiveSize.height, handleShape.size.height),
        )
    }

    val offsetX = handleSizeInternal.width / 2 + handleOffset.x

    val middleStart = HandleData(
        offset = DpOffset(x = -offsetX, y = 0.dp),
        alignment = Alignment.CenterStart,
        onDrag = onMiddleStartDrag
    )
    val middleEnd = HandleData(
        offset = DpOffset(x = offsetX, y = 0.dp),
        alignment = Alignment.CenterEnd,
        onDrag = onMiddleEndDrag
    )

    BoxWithCornerHandles(
        modifier = modifier,
        enabled = enabled,
        resetKey = cornerResetKey,
        handleOffset = handleOffset,
        handleShape = handleShape,
        contentAlignment = contentAlignment,
        minHandleInteractiveSize = minHandleInteractiveSize,
        onTopStartDrag = onTopStartDrag,
        onTopEndDrag = onTopEndDrag,
        onBottomStartDrag = onBottomStartDrag,
        onBottomEndDrag = onBottomEndDrag,
        onDragStart = onDragStart,
        onDragEnd = onDragEnd,
    ) {
        Box(
            contentAlignment = contentAlignment,
        ) {
            content()

            if (enabled) {
                listOf(
                    middleStart, middleEnd,
                ).forEach { data ->
                    Handle(
                        handleShape = handleShape,
                        resetKey = middleResetKey,
                        minSize = minHandleInteractiveSize,
                        alignment = data.alignment,
                        offset = data.offset,
                        onDrag = data.onDrag,
                        onDragStart = onDragStart,
                        onDragEnd = onDragEnd,
                    )
                }
            }
        }
    }
}