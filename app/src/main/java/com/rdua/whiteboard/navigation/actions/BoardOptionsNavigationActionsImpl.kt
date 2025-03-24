package com.rdua.whiteboard.navigation.actions

import com.rdua.whiteboard.board_options_dialog.navigation.BoardOptionsNavigationActions
import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import javax.inject.Inject

class BoardOptionsNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator,
): BoardOptionsNavigationActions {
    override suspend fun navigateToBoardItem(id: String) {
        appNavigator.navigateTo(
            route = Destination.BoardScreen(boardId = id),
            popUpToRoute = Destination.HomeScreen.route,
            isSingleTop = true
        )
    }
}