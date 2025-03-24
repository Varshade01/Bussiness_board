package com.rdua.whiteboard.board_item.usecase

import com.rdua.whiteboard.board.model.BoardItemModel
import com.rdua.whiteboard.board_item.model.BoardItemInfo

interface IGetBoardItemInfoUseCase {
    suspend operator fun invoke(item: BoardItemModel): BoardItemInfo
}