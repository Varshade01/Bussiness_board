package com.rdua.whiteboard.board_options_dialog.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.rdua.whiteboard.board_options_dialog.event.BoardOptionsDismissEvent
import com.rdua.whiteboard.board_options_dialog.event.BoardOptionsEvent
import com.rdua.whiteboard.board_options_dialog.viewmodel.BoardOptionsViewModel
import com.rdua.whiteboard.board_options_dialog.viewmodel.getViewModel
import com.rdua.whiteboard.common.composable.RectangleBottomSheet
import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.invite_via_email.composable.InviteToBoardViaEmailDialog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardOptionsDialog(
    board: BoardEntity,
    isOpenedDialog: Boolean,
    onDismissDialog: () -> Unit = { },
    viewModel: BoardOptionsViewModel = getViewModel(data = board),
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    BoardOptionsEventHandler(
        eventFlow = viewModel.callbackEventFlow,
        onDismissDialog = {
            coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                onDismissDialog()
            }
        }
    )

    // Have to manually keep BoardOptionsViewModel board data up to date.
    viewModel.onEvent(BoardOptionsEvent.UpdateBoardData(board))

    if (isOpenedDialog) {
        BoardOptionsDialogContent(
            board = board,
            viewModel = viewModel,
            bottomSheetState = bottomSheetState,
            onDismissDialog = onDismissDialog,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoardOptionsDialogContent(
    board: BoardEntity,
    viewModel: BoardOptionsViewModel,
    bottomSheetState: SheetState,
    onDismissDialog: () -> Unit = { },
) {
    RectangleBottomSheet(
        openBottomSheet = true,
        sheetState = bottomSheetState,
        onDismissRequest = onDismissDialog,
    ) {
        RenameBoardItem(
            onClick = {
                viewModel.onEvent(BoardOptionsEvent.OpenRenameBoardDialog)
            }
        )
        InviteViaEmailItem(
            onClick = {
                viewModel.onEvent(BoardOptionsEvent.OpenInviteViaEmailDialog)
            }
        )
        SharingOptionsItem(
            onClick = {
                viewModel.onEvent(BoardOptionsEvent.SharingOptions)
            }
        )
        SharingLinkItem(
            onClick = {
                viewModel.onEvent(BoardOptionsEvent.SharingLink)
            }
        )
        DuplicateItem(
            onClick = {
                viewModel.onEvent(BoardOptionsEvent.OpenDuplicateDialog)
            }
        )
        DeleteBoardItem(
            onClick = {
                viewModel.onEvent(BoardOptionsEvent.OpenDeleteDialog)
            }
        )
    }

    RenameBoardDialog(
        renameBoardState = viewModel.boardNameState,
        isOpenedDialog = viewModel.isOpenRenameDialog,
        onEvent = viewModel::onEvent
    )

    DeleteBoardDialog(
        isOpenedDialog = viewModel.isOpenDeleteDialog,
        onEvent = viewModel::onEvent,
    )

    DuplicateBoardDialog(
        isOpenedDialog = viewModel.isOpenDuplicateDialog,
        boardTitle = board.title,
        onEvent = viewModel::onEvent,
    )

    InviteToBoardViaEmailDialog(
        state = viewModel.inviteViaEmailState,
        isOpenedDialog = viewModel.isOpenInviteViaEmailDialog,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun BoardOptionsEventHandler(
    eventFlow: Flow<BoardOptionsDismissEvent>,
    onDismissDialog: () -> Unit = { },
) {
    LaunchedEffect(Unit) {
        eventFlow.collect {
            onDismissDialog()
        }
    }
}
