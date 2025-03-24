package com.rdua.whiteboard.board.composable.boxes

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.rdua.whiteboard.board.composable.handles.BoxWithCornerHandles
import com.rdua.whiteboard.board.composable.handles.SquareHandleShape
import com.rdua.whiteboard.board.composable.utils.HandlePosition
import com.rdua.whiteboard.board.composable.utils.calculateNewSize
import com.rdua.whiteboard.board.composable.utils.calculateResizeOffset
import com.rdua.whiteboard.board.utils.UpdateViewConfiguration

/**
 * Resizing Box for ShapeUI.
 * Functionality:
 * Resizing is done via 4 corner handles - returns new size and offset.
 *
 * Important info:
 * 1. ShapeResizeBox has a minimal size [minSize] that defines interactive clickable area and should not
 * be smaller than 25x25(dp) for better UX. For [minSize] to be less than 25x25(dp) will require
 * introducing inner box that would be able to resize independently from the outer interactive box (see
 * [TextResizeBox] implementation).
 *
 * Resizing feature:
 * 1. Resizing is used to manually change the size of the Shape without preserving the aspect ratio.
 * Drag offset is added as is and only corrected for [minSize].
 */
@Composable
fun ShapeResizeBox(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    handleOffset: DpOffset = DpOffset.Zero,
    handleSize: Dp = 8.dp,
    handleWidth: Dp = 1.dp,
    handleColor: Color = Color.Transparent,
    handleBorderColor: Color = Color.Black,
    contentSize: Size = Size.Zero,
    minSize: Size = Size(48f, 48f),
    contentAlignment: Alignment = Alignment.TopStart,
    onResize: (offset: Offset, size: Size) -> Unit = { _, _ -> },
    onResizeEnd: () -> Unit = { },
    content: @Composable BoxScope.() -> Unit = { }
) {
    // Actual size, that is updated as the user resizes this box.
    var currentSize by remember { mutableStateOf(contentSize) }
    // Size of the box before user starts resizing it.
    var preDragSize = remember { Size.Zero }
    // The distance between the origin point of touch event and current dragging position.
    var totalDragOffset = remember { Offset.Zero }

    // Resizing shape while dragging corner handles.
    fun processDrag(
        offset: Offset,
        handlePosition: HandlePosition,
    ) {
        totalDragOffset = totalDragOffset.plus(offset)

        // New shape size with minimal size constraint resized with current drag offset.
        val newContentSize: Size = calculateNewSize(
            minSize, preDragSize, totalDragOffset, handlePosition
        )

        // Offset change relative to the previous offset coordinate.
        val offsetChange = calculateResizeOffset(
            currentSize = currentSize,
            newSize = newContentSize,
            handlePosition = handlePosition
        )

        onResize(offsetChange, newContentSize)
    }

    UpdateViewConfiguration(
        minimumTouchTargetSize = 25.dp
    ) {
        BoxWithCornerHandles(
            modifier = modifier.onSizeChanged {
                currentSize = it.toSize()
            },
            enabled = enabled,
            handleOffset = handleOffset,
            handleShape = SquareHandleShape(
                size = DpSize(handleSize, handleSize),
                borderWidth = handleWidth,
                color = handleColor,
                borderColor = handleBorderColor,
            ),
            contentAlignment = contentAlignment,
            minHandleInteractiveSize = DpSize(25.dp, 25.dp),
            onTopStartDrag = { offset ->
                processDrag(offset, HandlePosition.TOP_START)
            },
            onTopEndDrag = { offset ->
                processDrag(offset, HandlePosition.TOP_END)
            },
            onBottomStartDrag = { offset ->
                processDrag(offset, HandlePosition.BOTTOM_START)
            },
            onBottomEndDrag = { offset ->
                processDrag(offset, HandlePosition.BOTTOM_END)
            },
            onDragStart = {
                preDragSize = currentSize
            },
            onDragEnd = {
                totalDragOffset = Offset.Zero
                onResizeEnd()
            },
            content = content
        )
    }
}