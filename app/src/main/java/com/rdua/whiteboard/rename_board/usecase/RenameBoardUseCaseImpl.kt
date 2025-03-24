package com.rdua.whiteboard.rename_board.usecase

import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.data.entities.boards.BoardMapper
import com.rdua.whiteboard.rename_board.state.RenameBoardState
import com.rdua.whiteboard.repository.boards.BoardsRepository
import javax.inject.Inject

class RenameBoardUseCaseImpl @Inject constructor(
    private val firebaseBoardsRepository: BoardsRepository,
    private val boardMapper: BoardMapper,
) : RenameBoardUseCase {

    override suspend fun renameBoard(boardEntity: BoardEntity, newName: String): RenameBoardState {
        return firebaseBoardsRepository.renameBoard(
            boardEntity = boardMapper.toFirebaseEntity(boardEntity),
            newName = newName
        )
    }
}