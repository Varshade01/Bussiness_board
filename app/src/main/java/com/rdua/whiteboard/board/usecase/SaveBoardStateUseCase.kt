package com.rdua.whiteboard.board.usecase

import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.firebase.state.UpdateBoardState

interface SaveBoardStateUseCase {
    suspend operator fun invoke(
        boardId: String,
        boardContent: List<BoardItemModel>,
        modifiedBy: UserUID,
        modifiedAt: Long,
    ): UpdateBoardState
}