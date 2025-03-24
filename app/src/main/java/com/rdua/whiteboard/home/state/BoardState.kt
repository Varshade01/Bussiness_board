package com.rdua.whiteboard.home.state

sealed class BoardState {

    data class BoardSuccess(val boardId: String) : BoardState()

    object CreateBoardFailure : BoardState()

    object KeyBoardFailure : BoardState()

    object CurrentUserIdFailure : BoardState()
}
