package com.rdua.whiteboard.board_options_dialog.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board_options_dialog.event.BoardOptionsEvent
import com.rdua.whiteboard.common.composable.ApproveDialog

@Composable
fun DeleteBoardDialog(
    isOpenedDialog: Boolean,
    onEvent: (BoardOptionsEvent) -> Unit = { },
) {
    if (isOpenedDialog) {
        ApproveDialog(
            titleDialog = stringResource(R.string.delete_board),
            textDialog = stringResource(R.string.delete_board_text_dialog),
            textConfirmButton = stringResource(R.string.delete),
            confirmOnClick = {
                onEvent(BoardOptionsEvent.DeleteBoard)
            },
            dismissOnClick = {
                onEvent(BoardOptionsEvent.DismissDeleteBoard)
            }
        )
    }
}