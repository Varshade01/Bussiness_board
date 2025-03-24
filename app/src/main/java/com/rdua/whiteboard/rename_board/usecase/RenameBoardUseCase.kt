package com.rdua.whiteboard.rename_board.usecase

import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.rename_board.state.RenameBoardState

interface RenameBoardUseCase {

    suspend fun renameBoard(boardEntity: BoardEntity, newName: String): RenameBoardState
}