package com.rdua.whiteboard.board.usecase

import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.firebase.state.UpdateBoardState

interface DeleteBoardItemUseCase {
    suspend operator fun invoke(
        boardId: String,
        itemId: String,
        modifiedBy: UserUID,
        modifiedAt: Long,
    ): UpdateBoardState
}