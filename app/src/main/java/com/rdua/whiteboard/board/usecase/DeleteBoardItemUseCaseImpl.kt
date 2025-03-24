package com.rdua.whiteboard.board.usecase

import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.data.entities.boards.provider.UpdateBoardDataProvider
import com.rdua.whiteboard.firebase.state.UpdateBoardState
import com.rdua.whiteboard.repository.boards.BoardsRepository
import javax.inject.Inject

class DeleteBoardItemUseCaseImpl @Inject constructor(
    private val boardsRepository: BoardsRepository,
    private val dataProvider: UpdateBoardDataProvider,
) : DeleteBoardItemUseCase {

    override suspend fun invoke(
        boardId: String,
        itemId: String,
        modifiedBy: UserUID,
        modifiedAt: Long,
    ): UpdateBoardState {
        val deleteBoardItemData = dataProvider.createDeleteBoardItemData(
            boardId = boardId,
            itemId = itemId,
            modifiedBy = modifiedBy,
            modifiedAt = modifiedAt
        )

        return boardsRepository.updateBoardState(data = deleteBoardItemData)
    }
}