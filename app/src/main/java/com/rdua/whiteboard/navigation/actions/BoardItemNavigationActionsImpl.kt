package com.rdua.whiteboard.navigation.actions

import com.rdua.whiteboard.board_item.navigation.IBoardItemNavigationActions
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import javax.inject.Inject

class BoardItemNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator,
) : IBoardItemNavigationActions {

    override suspend fun navigateBack() {
        appNavigator.navigateBack()
    }
}
