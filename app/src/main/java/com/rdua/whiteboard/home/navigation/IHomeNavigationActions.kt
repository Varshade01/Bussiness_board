package com.rdua.whiteboard.home.navigation

interface IHomeNavigationActions {

    suspend fun navigateToBoardItem(id: String)
}
