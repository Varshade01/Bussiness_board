package com.rdua.whiteboard.board.composable.boxes

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.board.composable.handles.BoxWithHorizontalHandles
import com.rdua.whiteboard.board.composable.handles.CircleHandleShape
import com.rdua.whiteboard.board.composable.utils.HandlePosition
import com.rdua.whiteboard.board.composable.utils.LineData
import com.rdua.whiteboard.board.composable.utils.calculateLineData
import com.rdua.whiteboard.board.constants.DefaultSizes
import com.rdua.whiteboard.board.utils.UpdateViewConfiguration
import com.rdua.whiteboard.ui.theme.Blue50


@Composable
fun LineResizeBox(
    startCoordinate: DpOffset = DpOffset.Zero,
    endCoordinate: DpOffset = DpOffset.Zero,
    enabled: Boolean = false,
    resizeResetKey: Any = Unit,
    handleSize: Dp = 8.dp,
    minHandleInteractiveSize: Dp = DefaultSizes.minHandleInteractiveSize,
    handleBorderWidth: Dp = 1.dp,
    handleColor: Color = Color.White,
    handleBorderColor: Color = Blue50,
    onResizeLine: (lineData: LineData?) -> Unit = { },
    onResizeEnd: () -> Unit = { },
    drawLine: DrawScope.() -> Unit = { },
    content: @Composable BoxScope.() -> Unit = { },
) {
    val density = LocalDensity.current

    // The total distance between the origin point of touch event and current dragging position.
    var totalDragOffset = remember { Offset.Zero }

    // Resizing while dragging handles.
    fun processDrag(
        offset: Offset,
        handlePosition: HandlePosition,
    ) {
        totalDragOffset = totalDragOffset.plus(offset)

        val lineData: LineData? = calculateLineData(
            itemCoordinate = startCoordinate,
            itemEndCoordinate = endCoordinate,
            offset = with(density) {
                DpOffset(
                    x = totalDragOffset.x.toDp(),
                    y = totalDragOffset.y.toDp()
                )
            },
            handlePosition = handlePosition,
        )

        onResizeLine(lineData)
    }

    UpdateViewConfiguration(
        minimumTouchTargetSize = minHandleInteractiveSize
    ) {
        BoxWithHorizontalHandles(
            enabled = enabled,
            resetKey = resizeResetKey,
            startCoordinate = startCoordinate,
            endCoordinate = endCoordinate,
            handleShape = CircleHandleShape(
                size = DpSize(handleSize, handleSize),
                borderWidth = handleBorderWidth,
                color = handleColor,
                borderColor = handleBorderColor,
                clipShape = CircleShape,
            ),
            onStartHandle = { offset ->
                processDrag(offset, HandlePosition.START)
            },
            onMiddleHandle = { offset ->
                processDrag(offset, HandlePosition.MIDDLE)
            },
            onEndHandle = { offset ->
                processDrag(offset, HandlePosition.END)
            },
            onDragEnd = {
                totalDragOffset = Offset.Zero
                onResizeEnd()
            },
            drawContentObject = drawLine,
            content = content,
        )
    }
}
