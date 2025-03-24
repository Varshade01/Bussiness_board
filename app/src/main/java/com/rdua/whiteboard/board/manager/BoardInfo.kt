package com.rdua.whiteboard.board.manager

import com.rdua.whiteboard.board.data.UserUID

data class BoardInfo(
    val boardId: String,
    val userName: String,
    val userUID: UserUID,
)
