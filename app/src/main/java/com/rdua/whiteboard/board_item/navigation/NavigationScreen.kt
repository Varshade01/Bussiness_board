package com.rdua.whiteboard.board_item.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.board_item.screen.BoardScreen

fun NavGraphBuilder.boardItemScreen() {
    composable(route = Destination.BoardScreen.fullRoute) {
        BoardScreen()
    }
}
