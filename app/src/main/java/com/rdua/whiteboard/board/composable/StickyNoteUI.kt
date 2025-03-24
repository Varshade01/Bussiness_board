package com.rdua.whiteboard.board.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.composable.boxes.StickyNoteResizeBox
import com.rdua.whiteboard.board.composable.utils.rememberAdjustedTextSize
import com.rdua.whiteboard.board.constants.DefaultTextStyles
import com.rdua.whiteboard.board.draw_shape.drawBlockedCanvas
import com.rdua.whiteboard.board.draw_shape.drawRectangleBorder
import com.rdua.whiteboard.board.model.StickyModel
import com.rdua.whiteboard.ui.theme.MaterialTheme

private const val TEXT_FIELD_SIZE_WEIGHT = 0.9f
private const val AUTHOR_FIELD_SIZE_WEIGHT = 0.1f

@Composable
fun StickyNoteUI(
    data: StickyModel,
    innerPaddings: Dp = 16.dp, // Use this parameter to specify paddings for inner content.
    authorTextStyle: TextStyle = DefaultTextStyles.stickyUIAuthorTextStyle,
    onSelect: (id: String) -> Unit = { },
    onEnableEditMode: (id: String) -> Unit = { },
    onDisableEditMode: (id: String) -> Unit = { },
    onTextChange: (id: String, newText: String) -> Unit = { _, _ -> },
    onMove: (id: String, offset: DpOffset) -> Unit = { _, _ -> },
    onScale: (id: String, scale: Float, offset: DpOffset) -> Unit = { _, _, _ -> },
    onFontSizeChange: (id: String, newFont: TextUnit) -> Unit = { _, _ -> },
    onSetMaxFontSize: (id: String, maxFont: TextUnit) -> Unit = { _, _ -> },
    onAutoFontSizeModeChange: (id: String, isEnabled: Boolean) -> Unit = { _, _ -> },
    onResizeEnd: (id: String) -> Unit = { },
    onDragEnd: (id: String) -> Unit = { },
) {
    val localDensity = LocalDensity.current
    val emptyTextPlaceholder = stringResource(R.string.add_text)

    // Size of the text area inside StickyNote.
    val inputFieldSize = rememberInputFieldSize(stickySize = data.size, paddings = innerPaddings)

    // Determines the current displayed text of this StickyNote (text, empty placeholder or " " when
    // empty in EditMode).
    val textToMeasure = when {
        data.isEditMode -> data.text.ifEmpty { " " }
        data.text.trimEnd().isNotEmpty() -> data.text
        else -> emptyTextPlaceholder
    }

    // Calculates current font size, max font size and whether AutoMode for font size should be activated.
    // Calculations are heavy and should be done when necessary.
    val adjustedFontSize by rememberAdjustedTextSize(
        text = textToMeasure,
        style = data.textStyle,
        inputFieldSize = inputFieldSize,
        isAutoFontSizeMode = data.isAutoFontSizeMode,
        maxFontSize = DefaultTextStyles.stickyFontSizes.last().sp
    )

    // Updates font changes that happen due to typing with AutoMode, resizing StickyNote etc.
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
        StickyNoteResizeBox(
            enabled = data.isSelected && !data.isLocked,
            handleColor = Color.White,
            handleBorderColor = MaterialTheme.colors.primaryBorder,
            contentSize = with(localDensity) { data.size.toSize() },
            minSize = with(localDensity) { DpSize(48.dp, 48.dp).toSize() },
            contentAlignment = Alignment.Center,
            onScale = { scaleChange, offsetChange ->
                with(localDensity) {
                    onScale(
                        data.id, scaleChange, DpOffset(
                            x = offsetChange.x.toDp(),
                            y = offsetChange.y.toDp()
                        )
                    )
                }
            },
            onResizeEnd = {
                onResizeEnd(data.id)
            }
        ) {
            BasicScalableBoardItemUI(
                modifier = Modifier
                    .shadow(
                        elevation = 16.dp,
                        clip = false // Removes default border clipping, when elevation > 0
                    ),
                boardItemId = data.id,
                size = data.size,
                isSelected = data.isSelected,
                isEditMode = data.isEditMode,
                isBlocked = data.isBlockedBy != null,
                isLocked = data.isLocked,
                scale = data.scale,
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
                    drawRect(
                        color = data.color,
                        size = size
                    )
                },
                borderCanvas = {
                    drawRectangleBorder(isLocked = data.isLocked)
                },
            ) {
                Column(
                    modifier = Modifier
                        .size(data.size)
                        .padding(innerPaddings),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(TEXT_FIELD_SIZE_WEIGHT)
                            .fillMaxWidth(),
                        contentAlignment = data.verticalTextAlignment
                    ) {
                        BoardEditableText(
                            modifier = Modifier.fillMaxWidth(),
                            text = data.text,
                            // When text changes, if font size changes, adjustedFontSize updates.
                            // It takes another recomposition to update font in data. This prevents
                            // visual glitches when font size changes in AutoMode.
                            textStyle = data.textStyle.copy(
                                fontSize = adjustedFontSize.currentSize
                            ),
                            placeholderTextStyle = DefaultTextStyles.stickyUITextStyle.copy(
                                color = MaterialTheme.colors.textFaint,
                                fontSize = data.textStyle.fontSize,
                                textAlign = data.textStyle.textAlign,
                            ),
                            placeholder = emptyTextPlaceholder,
                            isEditMode = data.isEditMode,
                            onTextChange = {
                                onTextChange(data.id, it)
                            },
                            onFinishEditing = {
                                onDisableEditMode(data.id)
                            }
                        )
                    }

                    Text(
                        modifier = Modifier.weight(AUTHOR_FIELD_SIZE_WEIGHT),
                        text = data.author ?: stringResource(id = R.string.unidentified),
                        style = authorTextStyle,
                    )
                }
            }
        }
    }
}

/**
 * Calculates size of the text inside the StickyNote including padding values and height weight of 90%.
 */
@Composable
private fun rememberInputFieldSize(
    stickySize: DpSize,
    paddings: Dp,
) : DpSize {
    return remember {
        DpSize(
            width = stickySize.width - (paddings * 2),
            height = (stickySize.height - (paddings * 2)).times(TEXT_FIELD_SIZE_WEIGHT),
        )
    }
}