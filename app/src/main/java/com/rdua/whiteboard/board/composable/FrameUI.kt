package com.rdua.whiteboard.board.composable

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.board.draw_shape.drawBlockedCanvas
import com.rdua.whiteboard.board.draw_shape.drawBorder
import com.rdua.whiteboard.board.model.FrameModel
import kotlin.math.roundToInt

@Composable
fun FrameUI(
    data: FrameModel,
    onSelectChange: (id: String) -> Unit,
    onEnableEditMode: (id: String) -> Unit,
    onMove: (id: String, offset: DpOffset) -> Unit,
    onDragEnd: (id: String) -> Unit,
) {
    BlockableBoardItem(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = data.coordinate.x
                        .toPx()
                        .roundToInt(),
                    y = data.coordinate.y
                        .toPx()
                        .roundToInt()
                )
            },
        isBlockedBy = data.isBlockedBy,
        blockedCanvas = {
            drawBlockedCanvas()
        },
    ) {
        BasicBoardItemUI(
            modifier = Modifier.size(data.size),
            boardItemId = data.id,
            isSelected = data.isSelected,
            isEditMode = data.isEditMode,
            isBlocked = data.isBlockedBy != null,
            isLocked = data.isLocked,
            onSelect = {
                onSelectChange(data.id)
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
            backgroundCanvas = {
                drawRect(
                    color = data.color,
                    size = size
                )
                drawRect(
                    color = data.borderColor,
                    style = Stroke(
                        width = 1.dp.toPx(),
                    )
                )
            },
            borderCanvas = {
                drawBorder(isLocked = data.isLocked)
            },
        )
    }
}
