package com.rdua.whiteboard.board_options_dialog.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.rdua.whiteboard.R
import com.rdua.whiteboard.board_options_dialog.event.BoardOptionsEvent
import com.rdua.whiteboard.common.composable.ButtonDialog
import com.rdua.whiteboard.common.composable.InputField
import com.rdua.whiteboard.rename_board.state.RenameBoardUIState
import com.rdua.whiteboard.rename_board.utils.stringResource
import com.rdua.whiteboard.ui.theme.MaterialTheme

@Composable
fun RenameBoardDialog(
    renameBoardState: RenameBoardUIState,
    isOpenedDialog: Boolean,
    titleDialog: String = stringResource(id = R.string.rename_board),
    textDialog: String = stringResource(id = R.string.rename_board_subtitle),
    textConfirmButton: String = stringResource(id = R.string.rename_btn),
    onEvent: (BoardOptionsEvent) -> Unit = { },
) {
    if (isOpenedDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                ButtonDialog(
                    textButton = textConfirmButton,
                    onClick = { onEvent(BoardOptionsEvent.RenameBoard) }
                )
            },
            dismissButton = {
                ButtonDialog(
                    textButton = stringResource(R.string.cancel),
                    onClick = { onEvent(BoardOptionsEvent.CloseRenameBoardDialog) }
                )
            },
            title = {
                Text(
                    text = titleDialog,
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Column {
                    Text(
                        text = textDialog,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    InputField(
                        text = renameBoardState.boardNameText,
                        isError = renameBoardState.boardNameIsError,
                        errorText = stringResource(resource = renameBoardState.boardNameErrorTextResource),
                        onValueChange = {
                            onEvent(BoardOptionsEvent.EnterRenameBoardText(it))
                        }
                    )
                }
            },
            containerColor = Color.White,
        )
    }
}