package com.rdua.whiteboard.home.usecase

import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.data.entities.boards.BoardMapper
import com.rdua.whiteboard.home.state.BoardState
import com.rdua.whiteboard.repository.boards.BoardsRepository
import javax.inject.Inject

class CreateBoardUseCaseImpl @Inject constructor(
    private val boardsRepository: BoardsRepository,
    private val boardMapper: BoardMapper,
) : ICreateBoardUseCase {

    override suspend fun createBoard(boardUIModel: BoardEntity, userBoardsTitles: List<String?>?): BoardState {
        val originTitle = boardUIModel.title
        var uniqueTitle = originTitle
        var count = 2

        while (userBoardsTitles?.contains(uniqueTitle) == true){
            uniqueTitle = "$originTitle $count"
            count++
        }

        val newBoard = boardMapper.toFirebaseEntity(boardUIModel.copy(title = uniqueTitle))
        return boardsRepository.createBoard(newBoard)
    }
}
