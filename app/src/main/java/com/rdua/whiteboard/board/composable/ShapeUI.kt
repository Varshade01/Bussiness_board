package com.rdua.whiteboard.board.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.board.composable.boxes.ShapeResizeBox
import com.rdua.whiteboard.board.composable.utils.rememberAdjustedTextSize
import com.rdua.whiteboard.board.constants.DefaultTextStyles
import com.rdua.whiteboard.board.draw_shape.calculateInnerShapeData
import com.rdua.whiteboard.board.draw_shape.drawBlockedCanvas
import com.rdua.whiteboard.board.draw_shape.drawRectangleBorder
import com.rdua.whiteboard.board.model.ShapeModel
import com.rdua.whiteboard.board.utils.getShapeContentAlignment
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun ShapeUI(
    data: ShapeModel,
    innerTextPaddings: Dp = 3.dp, // Use this parameter to specify paddings for inner text field.
    onSelect: (id: String) -> Unit = { },
    onEnableEditMode: (id: String) -> Unit = { },
    onDisableEditMode: (id: String) -> Unit = { },
    onTextChange: (id: String, newText: String) -> Unit = { _, _ -> },
    onMove: (id: String, offset: DpOffset) -> Unit = { _, _ -> },
    onSizeChange: (id: String, newSize: DpSize, offset: DpOffset) -> Unit = { _, _, _ -> },
    drawShape: DrawScope.() -> Unit = { },
    onFontSizeChange: (id: String, newFont: TextUnit) -> Unit = { _, _ -> },
    onSetMaxFontSize: (id: String, maxFont: TextUnit) -> Unit = { _, _ -> },
    onAutoFontSizeModeChange: (id: String, isEnabled: Boolean) -> Unit = { _, _ -> },
    onResizeEnd: (id: String) -> Unit = { },
    onDragEnd: (id: String) -> Unit = { },
) {
    val localDensity = LocalDensity.current

    // Calculates the size of the text field inside Shape. If paddings for inner text field are
    // needed, pass them here or font size calculation won't work properly.
    val innerShapeData by remember(data.size) {
        mutableStateOf(calculateInnerShapeData(model = data, paddings = innerTextPaddings))
    }

    // Calculates current font size, max font size and whether AutoMode for font size should be activated.
    // Calculations are heavy and should be done when necessary.
    val adjustedFontSize by rememberAdjustedTextSize(
        text = data.text,
        style = data.textStyle,
        inputFieldSize = innerShapeData.size,
        isAutoFontSizeMode = data.isAutoFontSizeMode,
        maxFontSize = DefaultTextStyles.absoluteMaxFontSize
    )

    // Updates font changes that happen due to typing with AutoMode, resizing Shape etc.
    LaunchedEffect(adjustedFontSize) {
        if (data.maxFontSize != adjustedFontSize.maxSize) {
            onSetMaxFontSize(data.id, adjustedFontSize.maxSize)
        }
        if (data.isAutoFontSizeMode != adjustedFontSize.autoAdjustMode) {
            onAutoFontSizeModeChange(data.id, adjustedFontSize.autoAdjustMode)
        }
        if (data.textStyle.fontSize != adjustedFontSize.currentSize) {
            onFontSizeChange(data.id, adjustedFontSize.currentSize)
        }
    }

    BlockableBoardItem(
        modifier = Modifier
            .offset(
                x = data.coordinate.x,
                y = data.coordinate.y
            ),
        contentPadding = data.blockingPadding,
        isBlockedBy = data.isBlockedBy,
        blockedCanvas = {
            drawBlockedCanvas()
        },
    ) {
        ShapeResizeBox(
            enabled = data.isSelected && !data.isLocked,
            handleColor = Color.White,
            handleBorderColor = MaterialTheme.colors.primaryBorder,
            contentSize = with(localDensity) { data.size.toSize() },
            minSize = with(localDensity) { DpSize(25.dp, 25.dp).toSize() },
            contentAlignment = Alignment.Center,
            onResize = { offset, newSize ->
                with(localDensity) {
                    onSizeChange(
                        data.id,
                        newSize.toDpSize(),
                        DpOffset(
                            x = offset.x.toDp(),
                            y = offset.y.toDp(),
                        )
                    )
                }
            },
            onResizeEnd = {
                onResizeEnd(data.id)
            }
        ) {
            BasicBoardItemUI(
                modifier = Modifier.size(data.size),
                boardItemId = data.id,
                isSelected = data.isSelected,
                isEditMode = data.isEditMode,
                isBlocked = data.isBlockedBy != null,
                isLocked = data.isLocked,
                contentAlignment = getShapeContentAlignment(data.type),
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
                backgroundCanvas = {
                    drawShape()
                },
                borderCanvas = {
                    drawRectangleBorder(isLocked = data.isLocked)
                },
            ) {
                Box(
                    modifier = Modifier
                        .offset(
                            x = innerShapeData.offset.x,
                            y = innerShapeData.offset.y
                        )
                        .size(size = innerShapeData.size),
                    contentAlignment = data.verticalTextAlignment
                ) {
                    BoardEditableText(
                        text = data.text,
                        modifier = Modifier.fillMaxWidth(),
                        // When text changes, if font size changes, adjustedFontSize updates.
                        // It takes another recomposition to update font in data. This prevents
                        // visual glitches when font size changes in AutoMode.
                        textStyle = data.textStyle.copy(
                            fontSize = adjustedFontSize.currentSize
                        ),
                        isEditMode = data.isEditMode,
                        onTextChange = {
                            onTextChange(data.id, it)
                        },
                        onFinishEditing = {
                            onDisableEditMode(data.id)
                        }
                    )
                }
            }
        }
    }
}
