package com.rdua.whiteboard.board_item.usecase

import com.rdua.whiteboard.data.entities.boards.BoardEntityState
import kotlinx.coroutines.flow.StateFlow

interface ISubscribeToBoardUseCase {
    fun subscribeToBoard(id: String?): StateFlow<BoardEntityState?>
}
