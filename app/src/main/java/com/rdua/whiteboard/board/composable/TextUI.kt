package com.rdua.whiteboard.board.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board.composable.boxes.TextResizeBox
import com.rdua.whiteboard.board.composable.utils.round
import com.rdua.whiteboard.board.composable.utils.roundValuesToInt
import com.rdua.whiteboard.board.constants.DefaultTextStyles
import com.rdua.whiteboard.board.draw_shape.drawBlockedCanvas
import com.rdua.whiteboard.board.draw_shape.drawNonOverlapRectangleBorder
import com.rdua.whiteboard.board.model.TextModel
import com.rdua.whiteboard.board.utils.conditional
import com.rdua.whiteboard.board.utils.onSizeActuallyChanged
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun TextUI(
    data: TextModel,
    textFieldPaddings: Dp = 1.dp,
    minFontSize: Float = 4f,
    maxFontSize: Float = DefaultTextStyles.absoluteMaxFontSize.value,
    minTextResizeBoxSize: DpSize = DpSize(25.dp, 25.dp),
    defaultTextStyle: TextStyle = DefaultTextStyles.textUITextStyle,
    onSelect: (id: String) -> Unit = { },
    onEnableEditMode: (id: String) -> Unit = { },
    onDisableEditMode: (id: String) -> Unit = { },
    onTextChange: (id: String, newText: String) -> Unit = { _, _ -> },
    onMove: (id: String, offset: DpOffset) -> Unit = { _, _ -> },
    onSizeChange: (id: String, newSize: DpSize, offset: DpOffset) -> Unit = { _, _, _ -> },
    onTextSizeChange: (id: String, newSize: DpSize) -> Unit = { _, _ -> },
    onTextScaleChange: (id: String, newFontSize: TextUnit, offset: DpOffset) -> Unit = { _, _, _ -> },
    onDisableAutoTextWidth: (id: String) -> Unit = { },
    onResizeEnd: (id: String) -> Unit = { },
    onDragEnd: (id: String) -> Unit = { },
) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val emptyTextPlaceholder = stringResource(R.string.double_tap_and_enter_text)

    // Determines the current displayed text of this TextUI (text, empty placeholder or " " when
    // empty in EditMode).
    val textToMeasure = when {
        data.isEditMode -> data.text.ifEmpty { " " }
        data.text.trimEnd().isNotEmpty() -> data.text
        else -> emptyTextPlaceholder
    }

    // Determines the current font style to calculate the size of current text. For empty text or
    // placeholder defaultTextStyle is used because only font size matters for them and font sizes
    // changes via scaling.
    val textStyleToMeasure = when {
        data.text.trimEnd().isNotEmpty() -> data.textStyle.copy(fontSize = defaultTextStyle.fontSize)
        else -> defaultTextStyle
    }

    // Serves two purposes:
    // 1. To scale TextUI using Modifier.graphicsLayer
    // 2. For instance to be captured by font scale lambda. It will be used to calculate new font size
    // It's vital to update captured instance, when font size is changed by any other means other
    // then dragging font scale handle.
    var fontScaleFactor by remember(data.textStyle) {
        mutableFloatStateOf(data.textStyle.fontSize.value / defaultTextStyle.fontSize.value)
    }

    // Cached font size will be updated by scaling handles lambda. Is used to differentiate when
    // font size is changed by font scaling handles or other way outside this TextUI.
    var cachedFontSize by remember {
        mutableStateOf(data.textStyle.fontSize)
    }

    //To be used when there is a need to reset scaling handles to update their captured values.
    var scaleResetKey by remember {
        mutableIntStateOf(fontScaleFactor.hashCode())
    }

    // Each recomposition check that current font size wasn't changed by any means except dragging
    // font scaling handles. If it was we need to reset values captured by handles via reset key.
    SideEffect {
        if (cachedFontSize != data.textStyle.fontSize) {
            scaleResetKey = fontScaleFactor.hashCode()
        }
    }

    // Calculates the size required for the text content of this TextUI.
    val textSize = remember(data.size, textToMeasure, textStyleToMeasure) {
        with(density) {
            measureSizeDp(
                measurer = textMeasurer,
                text = textToMeasure,
                style = textStyleToMeasure,
                constraints = Constraints(
                    maxWidth = if (data.isAutoWidthMode) {
                        (TextModel.autoMaxWidth - textFieldPaddings * 2).roundToPx()
                    } else {
                        (data.size.width - textFieldPaddings * 2).roundToPx()
                    }
                ),
            )
        }
    }


    // Final text content size.
    val textSizeWithPaddings by remember(data.size, data.isAutoWidthMode, textSize) {
        mutableStateOf(
            if (data.isAutoWidthMode) {
                textSize.addTextFieldPaddings(textFieldPaddings)
            } else {
                data.size.copy(height = textSize.height + (textFieldPaddings * 2))
            }
        )
    }

    // Reset scale drag handles lambda when changes:
    // 1) TextUI width (after resizing)
    // 2) text
    // 3) textStyle (bold, italic etc.)
    // 4) scaleResetKey (font size changed in text toolbar, not while dragging scale handle)
    val scaleHandleResetKey =
        textSizeWithPaddings.hashCode() + textToMeasure.hashCode() + textStyleToMeasure.hashCode() + scaleResetKey

    // Reset resize drag handles lambda when changes:
    // 1) Scale (either by dragging handles or by setting new font size)
    // 2) text
    // 3) textStyle (bold, italic etc. matters)
    val resizeHandleResetKey =
        fontScaleFactor.hashCode() + textToMeasure.hashCode() + textStyleToMeasure.hashCode()


    // Minimal allowed scaled size is calculated with current text size x1 scaled to minimal scale.
    val minScaleSize by rememberSizeWithScale(
        size = textSizeWithPaddings,
        scale = minFontSize / defaultTextStyle.fontSize.value
    )

    // Maximum allowed scaled size is calculated with current text size x1 scaled to maximum scale.
    val maxScaleSize by rememberSizeWithScale(
        size = textSizeWithPaddings,
        scale = maxFontSize / defaultTextStyle.fontSize.value
    )

    BlockableBoardItem(
        modifier = Modifier
            .offset(
                x = data.coordinate.x,
                y = data.coordinate.y
            ),
        isBlockedBy = data.isBlockedBy,
        blockedCanvas = {
            drawBlockedCanvas()
        },
    ) {
        TextResizeBox(
            enabled = data.isSelected && !data.isLocked,
            handleColor = Color.White,
            handleBorderColor = MaterialTheme.colors.secondaryBorder,
            contentSize = with(density) { textSizeWithPaddings.toSize() },
            scaleFactor = fontScaleFactor,
            scaleResetKey = scaleHandleResetKey,
            resizeResetKey = resizeHandleResetKey,
            minContainerSize = with(density) { minTextResizeBoxSize.toSize() },
            minScaleSize = minScaleSize,
            maxScaleSize = maxScaleSize,
            onResize = { offset, newSize ->
                if (data.isAutoWidthMode) {
                    onDisableAutoTextWidth(data.id)
                }

                with(density) {
                    onSizeChange(
                        data.id,
                        newSize.toDpSize(),
                        DpOffset(
                            x = offset.x.toDp(),
                            y = 0.dp
                        ),
                    )
                }
            },
            onScale = { scaleChange, offsetChange ->
                with(density) {
                    // To keep localScaleFactor up to date inside lambda while dragging, we need
                    // to update it manually.
                    // As data.textStyle changes, fontScaleFactor outside lambda will recalculate
                    // and use a different instance.
                    // This fontScaleFactor instance is captured by lambda and will only reset by
                    // updating scaleResetKey.
                    fontScaleFactor += scaleChange

                    // N.B. rounding intermediate font change will cause visual jitter, because offset is calculated
                    // for non rounded font size.
                    val newFontSize = defaultTextStyle.fontSize.times(fontScaleFactor).round(1)

                    // Caching font size change made by lambda. To update scaleResetKey we compare
                    // current data.fontSize with cachedFontSize to see if change was made by lambda
                    // or by external source (text toolbar)
                    cachedFontSize = newFontSize

                    onTextScaleChange(
                        data.id, newFontSize, DpOffset(
                            x = offsetChange.x.toDp(),
                            y = offsetChange.y.toDp()
                        )
                    )
                }
            },
            onResizeEnd = {
                onResizeEnd(data.id)
            },
        ) {
            BasicScalableBoardItemUI(
                modifier = Modifier
                    .conditional(
                        condition = data.isAutoWidthMode,
                        ifTrue = {
                            onSizeActuallyChanged { newSize ->
                                with(density) {
                                    onTextSizeChange(
                                        data.id,
                                        newSize
                                            .toSize()
                                            .toDpSize()
                                    )
                                }
                            }
                        },
                    ),
                boardItemId = data.id,
                size = textSizeWithPaddings,
                isSelected = data.isSelected,
                isEditMode = data.isEditMode,
                isBlocked = data.isBlockedBy != null,
                isLocked = data.isLocked,
                scale = fontScaleFactor,
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
                    drawNonOverlapRectangleBorder(isLocked = data.isLocked)
                },
            ) {
                BoardEditableText(
                    modifier = Modifier
                        .padding(textFieldPaddings) // Place before size.
                        .fillMaxSize(), // Fill all size in ResizeBox for text alignment to work properly.
                    text = data.text,
                    textStyle = data.textStyle.copy(fontSize = defaultTextStyle.fontSize),
                    placeholderTextStyle = DefaultTextStyles.textUITextStyle.copy(
                        color = MaterialTheme.colors.textFaint,
                        textAlign = data.textStyle.textAlign,
                    ),
                    isEditMode = data.isEditMode,
                    placeholder = emptyTextPlaceholder,
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

private fun Density.measureSizeDp(
    measurer: TextMeasurer,
    text: String = "",
    style: TextStyle = TextStyle.Default,
    constraints: Constraints = Constraints(),
): DpSize = measurer.measure(
    text = text,
    style = style,
    density = this,
    constraints = constraints
).size.toSize().toDpSize()


// Adds additional size to text content to be later consumed by paddings.
// Adding x3 horizontal paddings is a workaround for the Compose wierd behavior where x2 additional
// width is not enough for paddings of certain sizes (1-3.dp).
private fun DpSize.addTextFieldPaddings(textFieldPaddings: Dp) = this.plus(
    DpSize(
        width = textFieldPaddings * 3,
        height = textFieldPaddings * 2,
    )
)

// Rounding is used match rounding done by Compose in Modifier.size and Modifier.onSizeChanged.
@Composable
private fun rememberSizeWithScale(size: DpSize, scale: Float) : MutableState<Size> {
    val density = LocalDensity.current
    return remember(size) {
        mutableStateOf(
            with(density) {
                size.times(scale).toSize().roundValuesToInt()
            }
        )
    }
}