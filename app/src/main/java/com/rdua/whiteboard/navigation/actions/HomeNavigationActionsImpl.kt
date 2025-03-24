package com.rdua.whiteboard.navigation.actions

import com.rdua.whiteboard.home.navigation.IHomeNavigationActions
import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import javax.inject.Inject

class HomeNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator,
) : IHomeNavigationActions {

    override suspend fun navigateToBoardItem(id: String) {
        appNavigator.navigateTo(
            route = Destination.BoardScreen(boardId = id),
            isSingleTop = true
        )
    }
}
