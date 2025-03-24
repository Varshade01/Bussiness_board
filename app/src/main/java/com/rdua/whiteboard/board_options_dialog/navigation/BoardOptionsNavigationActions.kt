package com.rdua.whiteboard.board_options_dialog.navigation

interface BoardOptionsNavigationActions {
    suspend fun navigateToBoardItem(id: String)
}