package com.rdua.whiteboard.board.composable

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import com.rdua.whiteboard.board.utils.conditional

@Composable
fun BasicBoardItemUI(
    boardItemId: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isEditMode: Boolean = false,
    isBlocked: Boolean = false,
    isLocked: Boolean = false,
    contentAlignment: Alignment = Alignment.Center,
    onSelect: () -> Unit = { },
    onEnableEditMode: () -> Unit = { },
    onMove: (offset: DpOffset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    backgroundCanvas: DrawScope.() -> Unit = { },
    borderCanvas: DrawScope.() -> Unit = { },
    content: @Composable BoxScope.() -> Unit = { },
) {
    var totalDragOffset = remember { Offset.Zero }

    val processUserTapGesturesModifier = Modifier
        // Providing keys will restart the block with updated values
        .pointerInput(isSelected, isEditMode, isLocked, boardItemId) {
            detectTapGestures(
                onLongPress = {
                    if (!isSelected) {
                        onSelect()
                    }
                },
                onDoubleTap = {
                    if (!isLocked && !isEditMode) {
                        onEnableEditMode()
                    } else if (isLocked && !isSelected) {
                        onSelect()
                    }
                }
            )
        }

    val processUserDragGesturesModifier = Modifier
        // Drag without long press, if currently selected.
        // for it to not be consumed by onLongPress while isSelected == true.
        .pointerInput(isSelected) {
            detectDragGestures(onDragEnd = {
                if (isSelected) {
                    onDragEnd()
                }
            }) { change, dragAmount ->
                if (isSelected) {
                    change.consume()

                    onMove(
                        DpOffset(
                            x = dragAmount.x.toDp(),
                            y = dragAmount.y.toDp(),
                        )
                    )
                }
            }
        }
        // Wait for long press to start dragging.
        .pointerInput(boardItemId) {
            detectDragGesturesAfterLongPress(onDragEnd = {
                if (totalDragOffset != Offset.Zero) {
                    onDragEnd()
                }
                totalDragOffset = Offset.Zero
            }) { change, dragAmount ->
                change.consume()
                totalDragOffset = dragAmount

                onMove(
                    DpOffset(
                        x = dragAmount.x.toDp(),
                        y = dragAmount.y.toDp(),
                    )
                )
            }
        }

    Box(
        modifier = modifier
            .conditional(
                condition = !isBlocked,
                ifTrue = { processUserTapGesturesModifier },
            )
            // Place AFTER processUserTapGesturesModifier in chain.
            .conditional(
                condition = !isLocked && !isBlocked,
                ifTrue = { processUserDragGesturesModifier },
            )
            .drawBehind {
                backgroundCanvas()

                if (isSelected) {
                    borderCanvas()
                }
            },
        contentAlignment = contentAlignment,
        content = content
    )
}
