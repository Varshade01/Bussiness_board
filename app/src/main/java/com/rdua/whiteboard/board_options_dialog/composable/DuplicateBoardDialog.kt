package com.rdua.whiteboard.board_options_dialog.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board_options_dialog.event.BoardOptionsEvent
import com.rdua.whiteboard.common.composable.ApproveDialog

@Composable
fun DuplicateBoardDialog(
    isOpenedDialog: Boolean,
    boardTitle: String,
    onEvent: (BoardOptionsEvent) -> Unit = { },
) {
    if (isOpenedDialog) {
        ApproveDialog(
            titleDialog = stringResource(R.string.duplicate),
            textDialog = stringResource(R.string.duplicate_text_dialog, boardTitle),
            confirmOnClick = {
                onEvent(BoardOptionsEvent.Duplicate)
            },
            dismissOnClick = {
                onEvent(BoardOptionsEvent.CloseDuplicateDialog)
            }
        )
    }
}