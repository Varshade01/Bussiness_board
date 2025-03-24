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
import com.rdua.whiteboard.board.composable.utils.calculateScaleChange
import com.rdua.whiteboard.board.composable.utils.calculateScaleOffset
import com.rdua.whiteboard.board.utils.UpdateViewConfiguration

@Composable
fun StickyNoteResizeBox(
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
    onResizeEnd: () -> Unit = { },
    onScale: (scaleChange: Float, offsetChange: Offset) -> Unit = { _, _ -> },
    content: @Composable BoxScope.() -> Unit = { }
) {
    // Actual size, that is updated as the user resizes this box.
    var currentSize by remember { mutableStateOf(contentSize) }
    // Size of the box before user starts resizing it.
    var preDragSize = remember { Size.Zero }
    // Unscaled size of content is required to calculate scale and position after handle drag.
    var unscaledSize by remember(contentSize) { mutableStateOf(contentSize) }
    // The distance between the origin point of touch event and current dragging position.
    var totalDragOffset = remember { Offset.Zero }

    // Takes drag offset, calculates and passes new scale and position offset to onScale function.
    fun processDrag(
        offset: Offset,
        handlePosition: HandlePosition,
    ) {
        totalDragOffset = totalDragOffset.plus(offset)

        // New size value including offset and ScaleDirection, corrected with minimal size constraints.
        val correctedNewSize: Size = calculateNewSize(
            minSize, preDragSize, totalDragOffset, handlePosition
        )

        val scaleChange: Float = calculateScaleChange(correctedNewSize, currentSize, unscaledSize)

        if (scaleChange != 0.0f) {
            val offsetChange =
                calculateScaleOffset(scaleChange, unscaledSize, handlePosition)

            onScale(scaleChange, offsetChange)
        }
    }

    UpdateViewConfiguration(
        minimumTouchTargetSize = 25.dp
    ) {
        BoxWithCornerHandles(
            modifier = modifier.onSizeChanged {
                currentSize = it.toSize()

                if (unscaledSize == Size.Zero) {
                    unscaledSize = currentSize
                }
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