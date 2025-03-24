package com.rdua.whiteboard.board.usecase

import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.data.entities.boards.provider.UpdateBoardDataProvider
import com.rdua.whiteboard.firebase.state.UpdateBoardState
import com.rdua.whiteboard.repository.boards.BoardsRepository
import javax.inject.Inject

class SaveBoardStateUseCaseImpl @Inject constructor(
    private val boardsRepository: BoardsRepository,
    private val dataProvider: UpdateBoardDataProvider,
) : SaveBoardStateUseCase {

    override suspend operator fun invoke(
        boardId: String,
        boardContent: List<BoardItemModel>,
        modifiedBy: UserUID,
        modifiedAt: Long
    ): UpdateBoardState {
        val saveBoardStateData = dataProvider.createSaveBoardStateData(
            boardId = boardId,
            boardContent = boardContent,
            modifiedBy = modifiedBy,
            modifiedAt = modifiedAt
        )

        return boardsRepository.updateBoardState(
            data = saveBoardStateData
        )
    }
}