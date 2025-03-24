package com.rdua.whiteboard.board.composable.boxes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.rdua.whiteboard.board.composable.handles.BoxWithHexHandles
import com.rdua.whiteboard.board.composable.handles.CircleHandleShape
import com.rdua.whiteboard.board.composable.utils.HandlePosition
import com.rdua.whiteboard.board.composable.utils.calculateNewSize
import com.rdua.whiteboard.board.composable.utils.calculateResizeOffset
import com.rdua.whiteboard.board.composable.utils.calculateResizedWidth
import com.rdua.whiteboard.board.composable.utils.calculateScaleChange
import com.rdua.whiteboard.board.composable.utils.calculateScaleOffset
import com.rdua.whiteboard.board.utils.UpdateViewConfiguration

/**
 * Resizing Box for TextUI.
 * Functionality:
 * 1. Resizing is done via 2 middle handles - returns new width value and offset.
 * 2. Scaling is done via 4 corner handles - returns new scale factor and offset.
 *
 *
 *
 * Important info:
 * 1. TextResizeBox has a minimal size [minContainerSize] that defines interactive clickable area and should not
 * be smaller than 25x25(dp) for better UX. It's also used as minimal width during resizing providing analog of 1-2
 * character width at 1x scale.
 *
 * 2. TextResizeBox content has minimal size [minScaleSize] that is used in scaling to scale text to minimal font
 * size. It should be equal to current text content in minimal font size. Scaling lambda should reset when text
 * or text style changes to update captured [minScaleSize] value.
 *
 * 3. Minimal size restriction create a situation where user drags handles past the minimal point. The handles
 * then freeze in place and don't move until user drags back to it. To easily support this behavior instead
 * of delta drag and current size most calculations use "initial size" (at the start of dragging) and
 * "total drag offset" from the starting point to current coordinate.
 *
 * 4. TextResizeBox content can be resize/scaled less than [minContainerSize] (TextResizeBox min size), at which
 * point content is resized within the TextResizeBox and no offset should be passed back to TextUI.
 *
 * 5. Current implementation uses Modifier.graphicsLayer to scale TextUI content. That scales both
 * the content and the drag values received in handles. Correction is needed for some calculation.
 *
 *
 *
 * Resizing feature:
 * 1. Resizing is used to manually change the width of TextUI content. When used, autoWidthMode is
 * disabled and new width value defines the line break for wrapping text to the next line.
 *
 * 2. [minContainerSize] is used as TextResizeBox content's minimal width. That size is 25x25(dp) at 100% which should
 * fit at least 1 character of every alphabet (usually 2-3). That value doesn't change with font size,
 * because scale is done by a Modifier while all the values and calculations are in x1 scale.
 * (Ex. "Te" occupies 24.dp at x1. Increasing font size will take that original 24.dp and multiply
 * by a scale factor).
 *
 * 3. TextResizeBox content actual width can be less then [minContainerSize] when scale < x1. Calculation should
 * account for that. As TextResizeBox content is resized withing that [minContainerSize] no offset should be
 * passed back to TextUI. The TextUI should only move when TextResizeBox size changes.
 *
 * 4. Modifier.graphicsLayer scales the amount of dragOffset received in drag callbacks. For resize it's
 * necessary to scale it back to x1 scale by offset.div(scale).
 *
 * 5. Also [minContainerSize] size is always in x1, while all the values received in onSizeChanged are scaled.
 * So conversion is often necessary.
 *
 * 6. The Modifier.graphicsLayer doesn't scale the offset. When calculating offset use scaled values.
 * (Ex. "Te" occupies 24.dp at x1. It's scaled up to x2 (48.dp). User resizes by 1.dp. Modifier.graphicsLayer
 * scales that drag value by x2 to 2.dp. You scale it down and calculate new width as 25.dp. That
 * width value will later be scaled by Modifier.graphicsLayer to 50.dp. So real change after resizing
 * will be 2.dp. And the offset passed back to TextUI should be 2.dp.
 *
 * 7. Use [resizeResetKey] (pass hashCodes of multiple instances if needed) to reset the values captured
 * in resizing lambda. Do not reset the lambdas during drag or pointerInput will be lost.
 *
 *
 *
 * Scaling feature:
 * 1. Scaling is used to scale TextResizeBox by a certain factor. This preserves the original aspect ratio.
 *
 * 2. During scaling there is no need to correct the scaled dragOffset, because most calculations operate
 * scaled size values. And scale change is calculated as the difference between the previous (scaled) size
 * and the new (scaled) size.
 *
 * 3. Scaled content size can be less than [minContainerSize]. Calculation should account for that to not pass
 * any offset back to TextUI, while TextResizeBox is scaled within that [minContainerSize]. The TextUI should
 * only move when TextResizeBox size changes.
 *
 * 4. During scaling the "total drag offset" is added as is. To preserve the aspect ratio we calculate
 * the mean change for x,y between the "current size" and "new size" with "total drag offset".
 * (Ex. current size is (24, 15). Total drag offset is (10, 0). New size is (34, 15). Scaled size ~(27, 17).
 * That is because we take mean of total drag offset by x,y and our scale factor increases the size of the
 * TextResizeBox preserving the aspect ratio of ~3x2).
 *
 * 5. Scale change passed back to TextUI is relative to x1 scale. To have current scale you need to create
 * a variable (x1 initial) and ADD scale changes to it.
 *
 * 6. Calculating scale offset can be problematic as in TextResizeBox we only have access to scale change,
 * not current scale value. So 1. we calculate scale change that will be applied to TextResizeBox content.
 * Then 2. we calculate the size of the TextResizeBox with that scale change and correction of [minContainerSize].
 * Then 3. we get the actual different between current TextResizeBox and scaled TextResizeBox and that value
 * is the offset we pass back to TextUI.
 *
 * 7. Use [scaleResetKey] (pass hashCodes of multiple instances if needed) to reset the values captured
 * in scaling lambda. Do not reset the lambdas during drag or pointerInput will be lost.
 */

@Composable
fun TextResizeBox(
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    scaleResetKey: Any = Unit,
    resizeResetKey: Any = Unit,
    handleOffset: DpOffset = DpOffset(x = 12.dp, y = 10.dp),
    handleSize: Dp = 8.dp,
    handleWidth: Dp = 1.dp,
    handleColor: Color = Color.Transparent,
    handleBorderColor: Color = Color.Black,
    contentSize: Size = Size.Zero, // Content at x1 scale (doesn't change with font).
    scaleFactor: Float = 1f,
    minContainerSize: Size = Size(48f, 48f),
    minScaleSize: Size = Size(48f, 48f),
    maxScaleSize: Size = minScaleSize.times(2f),
    onResize: (offset: Offset, size: Size) -> Unit = { _, _ -> },
    onScale: (scaleChange: Float, offsetChange: Offset) -> Unit = { _, _ -> },
    onResizeEnd: () -> Unit = { },
    content: @Composable BoxScope.() -> Unit = { }
) {
    // Actual size, that is updated as the user resizes this box.
    var currentSize by remember { mutableStateOf(Size.Zero) }

    // The distance between the origin point of touch event and current dragging position.
    var totalDragOffset by remember { mutableStateOf(Offset.Zero) }

    // Size of the Box content before user starts resizing it (scaled).
    var preDragContentSize = remember { Size.Zero }
    // Current size of the content (scaled). Is updated in lambda by onSizeChanged.
    var currentContentSize by remember { mutableStateOf(Size.Zero) }

    // DpSize of minContainerSize.
    val minContainerSizeDp = with(LocalDensity.current) {
        remember {
            minContainerSize.toDpSize()
        }
    }

    // Resizing width while dragging horizontal handles.
    // Reset this lambda when scaleFactor or contentSize changes by anything except the lambda itself.
    fun processResizeDrag(
        offset: Offset,
        handlePosition: HandlePosition,
    ) {
        // Modifier.graphicsLayer used for scaling TextUI scales dragOffset as well. Corrected offset
        // scales offset back to x1.
        val correctedOffset = offset.div(scaleFactor)
        totalDragOffset = totalDragOffset.plus(correctedOffset)

        // New content width value including raw offset, corrected to 1x scale with minimal size constraint.
        val newContentWidth = calculateResizedWidth(
            minContainerSize.width, //<- is not scaled (x1 scale)
            preDragContentSize.width.div(scaleFactor), //<- is received in onSizeChanged and is scaled.
            totalDragOffset.x, //<- is corrected for x1 scale
            handlePosition
        )

        // New container width value with minimal size constraint scaled to actual scale.
        val newContainerWidth = maxOf(minContainerSize.width, newContentWidth.times(scaleFactor))

        // New horizontal offset relative to the previous offset coordinate. As content size can be less
        // than container size, TextUI position should only change when the size of the TextResizeBox
        // (container) changes.
        val offsetChange = calculateResizeOffset(
            currentSize = currentSize,
            // TextResizeBox only supports width resize. Height change is irrelevant.
            newSize = currentSize.copy(width = newContainerWidth),
            handlePosition = handlePosition
        )

        onResize(offsetChange, contentSize.copy(width = newContentWidth))
    }

    // Scaling content while dragging corner handles.
    // Reset this lambda when contentSize (unscaled content size) changes.
    fun processScaleDrag(
        offset: Offset,
        handlePosition: HandlePosition,
    ) {
        totalDragOffset = totalDragOffset.plus(offset)

        // New (scaled) content size including raw offset, corrected with minimal size constraints.
        val newContentSize: Size = calculateNewSize(
            minScaleSize, maxScaleSize, preDragContentSize, totalDragOffset, handlePosition
        )

        // Scale factor change for the new content size.
        val scaleChange: Float =
            calculateScaleChange(newContentSize, currentContentSize, contentSize)

        if (scaleChange != 0.0f) {
            val offsetChange = calculateScaleOffset(
                scaleChange = scaleChange,
                currentContentSize = currentContentSize,
                currentContainerSize = currentSize,
                minContainerSize = minContainerSize,
                initialSize = contentSize,
                handlePosition = handlePosition
            )

            onScale(scaleChange, offsetChange)
        }
    }

    UpdateViewConfiguration(
        minimumTouchTargetSize = 25.dp
    ) {
        BoxWithHexHandles(
            modifier = modifier
                // Setting minimal size for TextResizeBox also defines minimal clickable area.
                .sizeIn(minWidth = minContainerSizeDp.width, minHeight = minContainerSizeDp.height)
                .onSizeChanged {
                    currentSize = it.toSize()
                },
            middleResetKey = resizeResetKey,
            cornerResetKey = scaleResetKey,
            enabled = enabled,
            handleOffset = handleOffset,
            handleShape = CircleHandleShape(
                size = DpSize(handleSize, handleSize),
                borderWidth = handleWidth,
                color = handleColor,
                borderColor = handleBorderColor,
                clipShape = RectangleShape,
            ),
            contentAlignment = Alignment.Center,
            minHandleInteractiveSize = DpSize(25.dp, 25.dp),
            onTopStartDrag = { offset ->
                processScaleDrag(offset, HandlePosition.TOP_START)
            },
            onTopEndDrag = { offset ->
                processScaleDrag(offset, HandlePosition.TOP_END)
            },
            onMiddleStartDrag = { offset ->
                processResizeDrag(offset, HandlePosition.MIDDLE_START)
            },
            onMiddleEndDrag = { offset ->
                processResizeDrag(offset, HandlePosition.MIDDLE_END)
            },
            onBottomStartDrag = { offset ->
                processScaleDrag(offset, HandlePosition.BOTTOM_START)
            },
            onBottomEndDrag = { offset ->
                processScaleDrag(offset, HandlePosition.BOTTOM_END)
            },
            onDragStart = {
                preDragContentSize = currentContentSize
            },
            onDragEnd = {
                totalDragOffset = Offset.Zero
                onResizeEnd()
            },
        ) {
            // Inner Box is used to allow TextResizeBox content to be smaller than TextResizeBox itself
            // and get content's current size in resize/scale lambdas.
            Box(
                modifier = Modifier
                    .onSizeChanged {
                        currentContentSize = it.toSize()
                    },
                content = content,
            )
        }
    }
}