package com.rdua.whiteboard.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.board.utils.conditional
import com.rdua.whiteboard.board_item.screen.toolbar.color_bar.ColorOption
import com.rdua.whiteboard.ui.theme.Gray30Alpha50
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun ColorButton(
    enabled: Boolean = true,
    colorOption: ColorOption,
    color: Color,
    onClick: () -> Unit,
) {
    if (enabled) {
        val modifier = Modifier
            .size(size = MaterialTheme.spaces.space6)
            .clip(shape = colorOption.shape)
            .conditional(
                condition = (color == Color.White || color == Color.Transparent),
                ifTrue = {
                    border(
                        width = (0.5).dp,
                        color = Gray30Alpha50,
                        shape = colorOption.shape,
                    )
                },
            )
            .conditional(
                condition = color == Color.Transparent,
                ifTrue = {
                    drawBehind {
                        drawLine(
                            color = Gray30Alpha50,
                            strokeWidth = (0.5).dp.toPx(),
                            start = Offset(x = 0f, y = 0f),
                            end = Offset(x = size.width, y = size.height),
                        )
                    }
                },
            )
            .clickable { onClick() }

        Box(
            modifier = when (colorOption) {
                is ColorOption.BorderColor -> modifier.border(
                    width = 2.dp,
                    shape = colorOption.shape,
                    color = color
                )

                is ColorOption.BackgroundColor,
                is ColorOption.FontColor,
                -> modifier.background(color = color)
            }
        )
    }
}

@Preview
@Composable
fun ColorButtonPreview() {
    ColorButton(
        colorOption = ColorOption.BackgroundColor,
        color = Color.Transparent,
        onClick = { }
    )
}
