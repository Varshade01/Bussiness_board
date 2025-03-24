package com.rdua.whiteboard.home.state

import com.rdua.whiteboard.data.entities.boards.BoardEntity

data class AllBoardsUIState(
    val boardsList: List<BoardEntity>? = null,
    val boardOptionsBoardItem: BoardEntity? = null,
    val isOpenBoardOptions: Boolean = false,
)