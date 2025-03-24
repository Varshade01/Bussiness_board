package com.rdua.whiteboard.board.usecase

import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.firebase.state.UpdateBoardState

interface SaveBoardItemUseCase {

    suspend fun save(
        boardId: String,
        item: BoardItemModel,
        itemPosition: Int,
        modifiedBy: UserUID,
        modifiedAt: Long,
    ) : UpdateBoardState

    suspend fun save(
        boardId: String,
        item: BoardItemModel,
        itemPosition: Int,
    ) : UpdateBoardState
}