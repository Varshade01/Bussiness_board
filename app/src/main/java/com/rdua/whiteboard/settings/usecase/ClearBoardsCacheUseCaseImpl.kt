package com.rdua.whiteboard.settings.usecase

import com.rdua.whiteboard.repository.boards.BoardsRepository
import javax.inject.Inject

class ClearBoardsCacheUseCaseImpl @Inject constructor(
    private val boardsRepository: BoardsRepository,
) : IClearBoardsCacheUseCase {

    override fun clearBoardsCache() {
        boardsRepository.clearBoardsCache()
    }
}
