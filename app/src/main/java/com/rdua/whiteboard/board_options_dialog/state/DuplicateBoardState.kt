package com.rdua.whiteboard.board_options_dialog.state

sealed interface DuplicateBoardState {
    class Success(val boardId: String): DuplicateBoardState
    object Failure: DuplicateBoardState
}