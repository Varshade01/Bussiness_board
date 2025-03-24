package com.rdua.whiteboard.rename_board.state

sealed class RenameBoardState {

    object RenameBoardSuccess: RenameBoardState()

    object RenameBoardFailure : RenameBoardState()
}