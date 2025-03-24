package com.rdua.whiteboard.board.usecase

import com.rdua.whiteboard.board.data.UserUID
import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.data.entities.boards.provider.UpdateBoardDataProvider
import com.rdua.whiteboard.firebase.state.UpdateBoardState
import com.rdua.whiteboard.repository.boards.BoardsRepository
import javax.inject.Inject

class SaveBoardItemUseCaseImpl @Inject constructor(
    private val boardsRepository: BoardsRepository,
    private val dataProvider: UpdateBoardDataProvider,
) : SaveBoardItemUseCase {

    override suspend fun save(
        boardId: String,
        item: BoardItemModel,
        itemPosition: Int,
        modifiedBy: UserUID,
        modifiedAt: Long,
    ) : UpdateBoardState {
        val saveBoardItemData = dataProvider.createSaveBoardItemData(
            boardId = boardId,
            item = item,
            itemPosition = itemPosition,
            modifiedBy = modifiedBy,
            modifiedAt = modifiedAt
        )

        return boardsRepository.updateBoardState(data = saveBoardItemData)
    }

    override suspend fun save(
        boardId: String,
        item: BoardItemModel,
        itemPosition: Int
    ): UpdateBoardState {
        val saveBoardItemData = dataProvider.createSaveBoardItemData(
            boardId = boardId,
            item = item,
            itemPosition = itemPosition,
        )

        return boardsRepository.updateBoardState(data = saveBoardItemData)
    }
}