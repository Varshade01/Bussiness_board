package com.rdua.whiteboard.board.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.DpOffset
import com.rdua.whiteboard.board.constants.BlockingPadding
import com.rdua.whiteboard.board.data.UserInfo


@Composable
fun BlockableBoxWithOffsetLabel(
    modifier: Modifier = Modifier,
    labelCoordinate: DpOffset = DpOffset.Zero,
    contentPadding: BlockingPadding = BlockingPadding(),
    blockedBy: UserInfo? = null,
    blockedCanvas: DrawScope.() -> Unit = { },
    content: @Composable BoxScope.() -> Unit = { },
) {

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .drawWithContent {
                    drawContent()
                    if (blockedBy != null) {
                        blockedCanvas()
                    }
                }
                .padding(
                    with(contentPadding) {
                        PaddingValues(top = top, start = start, end = end, bottom = bottom)
                    }
                ),
            content = content
        )
    }

    if (blockedBy != null) {
        Box(
            modifier = Modifier
                .offset(x = labelCoordinate.x, y = labelCoordinate.y)
        ) {
            UserInfoLabel(blockedBy)
        }
    }
}
