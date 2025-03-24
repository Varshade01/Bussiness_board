package com.rdua.whiteboard.board_item.usecase

import com.rdua.whiteboard.repository.boards.BoardsRepository
import javax.inject.Inject

class ClearBoardItemUseCaseImpl @Inject constructor(
    private val boardsRepository: BoardsRepository,
) : IClearBoardItemUseCase {

    override fun clearBoardItem() {
        boardsRepository.clearBoardItem()
    }
}
