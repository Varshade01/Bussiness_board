package com.rdua.whiteboard.board.composable.handles

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

@Composable
fun BoxScope.Handle(
    modifier: Modifier = Modifier,
    resetKey: Any = Unit,
    handleShape: HandleShape = SquareHandleShape(),
    minSize: DpSize = DpSize(40.dp, 40.dp),
    alignment: Alignment = Alignment.TopStart,
    offset: DpOffset = DpOffset.Zero,
    onDrag: (Offset) -> Unit = { },
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
) {
    val handleSize = DpSize(
        width = max(minSize.width, handleShape.size.width),
        height = max(minSize.height, handleShape.size.height),
    )

    Box(
        modifier = modifier
            .size(handleSize)
            .align(alignment)
            .offset(x = offset.x, y = offset.y)
            .drawBehind(onDraw = handleShape.drawShape())
            .clip(handleShape.clipShape)
            .pointerInput(resetKey) {
                detectDragGestures(
                    onDragStart = onDragStart,
                    onDragEnd = onDragEnd,
                ) { change, dragAmount ->
                    change.consume()

                    onDrag(dragAmount)
                }
            }
    )
}