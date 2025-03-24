package com.rdua.whiteboard.board_item.usecase

import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.board_item.model.BoardItemInfo
import com.rdua.whiteboard.common.usecases.GetUserNameUseCase
import javax.inject.Inject

class GetBoardItemInfoUseCaseImpl @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase
) : IGetBoardItemInfoUseCase {
    override suspend fun invoke(item: BoardItemModel): BoardItemInfo {
        return BoardItemInfo(
            createdBy = getUserNameUseCase(item.creator),
            createdTime = item.createdAt.toFormattedString(),
            modifiedBy = getUserNameUseCase(item.modifiedBy),
            modifiedTime = item.modifiedAt.toFormattedString(),
        )
    }
}