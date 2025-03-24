package com.rdua.whiteboard.board_options_dialog.usecase

import com.rdua.whiteboard.board_options_dialog.state.DeleteBoardState
import com.rdua.whiteboard.repository.boards.BoardsRepository
import javax.inject.Inject

class DeleteBoardUseCaseImpl @Inject constructor(
    private val boardsRepository: BoardsRepository,
) : IDeleteBoardUseCase {

    override suspend fun deleteBoard(id: String): DeleteBoardState =
        boardsRepository.deleteBoard(id)
}
