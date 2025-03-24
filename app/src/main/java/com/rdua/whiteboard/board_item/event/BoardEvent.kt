package com.rdua.whiteboard.board_item.event

sealed interface BoardEvent {
    object OpenBoardOptions: BoardEvent
    object CloseBoardOptions: BoardEvent
}