package com.rdua.whiteboard.board.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import com.rdua.whiteboard.board.utils.conditional

@Composable
fun BasicScalableBoardItemUI(
    boardItemId: String,
    size: DpSize,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isEditMode: Boolean = false,
    isBlocked: Boolean = false,
    isLocked: Boolean = false,
    scale: Float = 1f,
    contentAlignment: Alignment = Alignment.Center,
    onSelect: () -> Unit = { },
    onEnableEditMode: () -> Unit = { },
    onMove: (offset: DpOffset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    backgroundCanvas: DrawScope.() -> Unit = { },
    borderCanvas: DrawScope.() -> Unit = { },
    content: @Composable BoxScope.() -> Unit = { },
) {
    val scaledSize: DpSize = remember(size, scale) { size.times(scale) }

    val scaleModifier = Modifier
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
        )

    BasicBoardItemUI(
        modifier = modifier
            // Manually set the size of outer composable to scaled size to not mess up the composition.
            .size(scaledSize)
            .drawWithContent {
                drawContent()

                if (isSelected) {
                    borderCanvas()
                }
            },
        boardItemId = boardItemId,
        isSelected = isSelected,
        isEditMode = isEditMode,
        isBlocked = isBlocked,
        isLocked = isLocked,
        contentAlignment = contentAlignment,
        onSelect = onSelect,
        onEnableEditMode = onEnableEditMode,
        onMove = onMove,
        onDragEnd = onDragEnd,
        backgroundCanvas = backgroundCanvas,
    ) {
        Box(
            modifier = Modifier
                // Ignore all constraints and set the unscaled size. Composable will scale relatively
                // to this unscaled size.
                .requiredSize(size)
                // Scale modifier influences drag offset calculations. It's better to put content
                // to scale into a separate box.
                // Scale applies only on DRAW phase. It doesn't affect the actual size of the
                // composable, just the way it's drawn on UI.
                .conditional(
                    condition = scale != 1f,
                    ifTrue = { scaleModifier }
                ),
            content = content
        )
    }
}