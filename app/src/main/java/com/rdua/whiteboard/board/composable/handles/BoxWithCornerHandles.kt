package com.rdua.whiteboard.board.composable.handles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
fun BoxWithCornerHandles(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    resetKey: Any = Unit,
    handleOffset: DpOffset = DpOffset.Zero,
    handleShape: HandleShape = SquareHandleShape(),
    contentAlignment: Alignment = Alignment.TopStart,
    minHandleInteractiveSize: DpSize = DpSize(40.dp, 40.dp),
    onTopStartDrag: (Offset) -> Unit = { },
    onTopEndDrag: (Offset) -> Unit = { },
    onBottomStartDrag: (Offset) -> Unit = { },
    onBottomEndDrag: (Offset) -> Unit = { },
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    content: @Composable BoxScope.() -> Unit = { },
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

    // After aligning to a given corner, this offset will CENTER the handle on the edge of that
    // corner. After which the additional [handleOffset] will be applied to the handle position.
    val handleOffsetInternal = remember {
        DpOffset(
            x = (handleSizeInternal.width) / 2 + handleOffset.x,
            y = (handleSizeInternal.height) / 2 + handleOffset.y,
        )
    }

    val topStart = HandleData(
        offset = DpOffset(x = -handleOffsetInternal.x, y = -handleOffsetInternal.y),
        onDrag = onTopStartDrag
    )
    val topEnd = HandleData(
        offset = DpOffset(x = handleOffsetInternal.x, y = -handleOffsetInternal.y),
        alignment = Alignment.TopEnd,
        onDrag = onTopEndDrag
    )
    val bottomStart = HandleData(
        offset = DpOffset(x = -handleOffsetInternal.x, y = handleOffsetInternal.y),
        alignment = Alignment.BottomStart,
        onDrag = onBottomStartDrag
    )
    val bottomEnd = HandleData(
        offset = DpOffset(x = handleOffsetInternal.x, y = handleOffsetInternal.y),
        alignment = Alignment.BottomEnd,
        onDrag = onBottomEndDrag
    )

    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
    ) {
        content()

        if (enabled) {
            listOf(
                topStart, topEnd,
                bottomStart, bottomEnd
            ).forEach { data ->
                Handle(
                    handleShape = handleShape,
                    resetKey = resetKey,
                    minSize = minHandleInteractiveSize,
                    alignment = data.alignment,
                    offset = data.offset,
                    onDrag = data.onDrag,
                    onDragStart = onDragStart,
                    onDragEnd = onDragEnd
                )
            }
        }
    }
}