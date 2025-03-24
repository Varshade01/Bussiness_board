package com.rdua.whiteboard.board.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.rdua.whiteboard.board.constants.BlockingPadding
import com.rdua.whiteboard.board.data.UserInfo

@Composable
fun BlockableBoardItem(
    modifier: Modifier = Modifier,
    contentPadding: BlockingPadding = BlockingPadding(),
    isBlockedBy: UserInfo? = null,
    blockedCanvas: DrawScope.() -> Unit = { },
    content: @Composable BoxScope.() -> Unit = { },
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .drawWithContent {
                    drawContent()
                    if (isBlockedBy != null) {
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

        if (isBlockedBy != null) {
            UserInfoLabel(isBlockedBy)
        }
    }
}
