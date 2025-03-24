package com.rdua.whiteboard.board_options_dialog.usecase

import com.rdua.whiteboard.board_options_dialog.state.DuplicateBoardState
import com.rdua.whiteboard.data.entities.boards.BoardEntity

interface IDuplicateBoardUseCase {
    suspend operator fun invoke(boardData: BoardEntity): DuplicateBoardState
}