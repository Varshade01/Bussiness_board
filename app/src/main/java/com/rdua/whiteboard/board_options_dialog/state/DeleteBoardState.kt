package com.rdua.whiteboard.board_options_dialog.state

sealed interface DeleteBoardState {

    object DeleteSuccess : DeleteBoardState

    object DeleteFailure : DeleteBoardState
}
