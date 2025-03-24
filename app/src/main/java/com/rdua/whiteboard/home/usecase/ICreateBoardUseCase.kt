package com.rdua.whiteboard.home.usecase

import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.home.state.BoardState

interface ICreateBoardUseCase {

    suspend fun createBoard(boardUIModel: BoardEntity, userBoardsTitles: List<String?>? = null): BoardState
}
