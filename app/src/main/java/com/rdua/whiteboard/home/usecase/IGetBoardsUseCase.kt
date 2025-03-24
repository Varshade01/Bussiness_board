package com.rdua.whiteboard.home.usecase

import com.rdua.whiteboard.data.entities.boards.BoardEntity
import kotlinx.coroutines.flow.StateFlow

interface IGetBoardsUseCase {

    fun getBoards(userId: String): StateFlow<List<BoardEntity>?>
}
