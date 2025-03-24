package com.rdua.whiteboard.home.event

import com.rdua.whiteboard.data.entities.boards.BoardEntity

sealed interface AllBoardsEvent {

    data class NavigateToNewBoard(val boardTitle: String) : AllBoardsEvent

    data class NavigateToBoardItem(val boardId: String?) : AllBoardsEvent

    class OpenBoardOptions(val boardEntity: BoardEntity) : AllBoardsEvent
    object CloseBoardOptions : AllBoardsEvent
    object MoreSettingsEvent : AllBoardsEvent
}
