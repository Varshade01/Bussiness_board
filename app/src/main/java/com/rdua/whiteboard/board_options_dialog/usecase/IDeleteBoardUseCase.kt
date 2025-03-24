package com.rdua.whiteboard.board_options_dialog.usecase

import com.rdua.whiteboard.board_options_dialog.state.DeleteBoardState

interface IDeleteBoardUseCase {

    suspend fun deleteBoard(id: String): DeleteBoardState
}
