package com.rdua.whiteboard.board.composable

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.DpOffset
import com.rdua.whiteboard.board.composable.boxes.LineResizeBox
import com.rdua.whiteboard.board.composable.utils.LineData
import com.rdua.whiteboard.board.draw_shape.drawBlockedCanvas
import com.rdua.whiteboard.board.draw_shape.drawLineShape
import com.rdua.whiteboard.board.draw_shape.drawRectangleBorder
import com.rdua.whiteboard.board.model.LineModel


@Composable
fun LineUI(
    data: LineModel,
    onSelect: (id: String) -> Unit = { },
    onEnableEditMode: (id: String) -> Unit = { },
    onMove: (id: String, offset: DpOffset) -> Unit = { _, _ -> },
    onDragEnd: (id: String) -> Unit = { },
    onResizeLine: (lineData: LineData?) -> Unit = { },
    onResizeEnd: (id: String) -> Unit = { },
) {

    // Cache local size and position and update them manually during resize. You can use this
    // value to check whether size/position were changed by this user resizing (don't reset lambda)
    // or by moving/other user moving or resizing line (reset lambda to update captured values)
    var cachedLineData: LineData by remember {
        mutableStateOf(data.extractLineData())
    }

    // Changing this value resets lambda. With that all the captured values in lambda are updated,
    // but pointerInput also resets and requires user to press and drag again.
    var resizeResetKey: Int by remember {
        mutableIntStateOf(cachedLineData.hashCode())
    }

    // Check this to know when line size and position was changed by other user.
    SideEffect {
        if (cachedLineData != data.extractLineData()) {
            resizeResetKey = data.extractLineData().hashCode()
        }
    }

    LineResizeBox(
        enabled = data.isSelected && !data.isLocked,
        startCoordinate = data.coordinate,
        endCoordinate = data.endCoordinate,
        resizeResetKey = resizeResetKey,
        onResizeLine = { lineData ->
            lineData?.let { cachedLineData = lineData }
            onResizeLine(lineData)
        },
        onResizeEnd = {
            resizeResetKey = cachedLineData.hashCode()
            onResizeEnd(data.id)
        },
        drawLine = {
            drawLineShape(
                color = data.color,
                lineWidth = data.size.height,
                lineStartCoordinate = data.coordinate,
                lineEndCoordinate = data.endCoordinate,
            )
        }
    ) {
        BlockableBoxWithOffsetLabel(
            modifier = Modifier
                .offset(x = data.coordinate.x, y = data.coordinate.y)
                .graphicsLayer {
                    this.transformOrigin = TransformOrigin(0f, 0f)
                    this.rotationZ = data.rotationAngle
                },
            labelCoordinate = listOf(data.coordinate, data.endCoordinate).maxBy { it.x },
            contentPadding = data.blockingPadding,
            blockedBy = data.isBlockedBy,
            blockedCanvas = {
                drawBlockedCanvas()
            },
        ) {
            BasicBoardItemUI(
                modifier = Modifier
                    .size(data.size),
                boardItemId = data.id,
                isSelected = data.isSelected,
                isEditMode = data.isEditMode,
                isBlocked = data.isBlockedBy != null,
                isLocked = data.isLocked,
                onSelect = {
                    onSelect(data.id)
                },
                onEnableEditMode = {
                    onEnableEditMode(data.id)
                },
                onMove = { offset ->
                    onMove(data.id, offset)
                },
                onDragEnd = {
                    onDragEnd(data.id)
                },
                borderCanvas = {
                    if (data.isLocked) {
                        drawRectangleBorder(isLocked = true)
                    }
                },
            )
        }
    }
}

private fun LineModel.extractLineData(): LineData = LineData(
    startCoordinate = coordinate,
    endCoordinate = endCoordinate,
    size = size,
    rotationAngle = rotationAngle,
)
