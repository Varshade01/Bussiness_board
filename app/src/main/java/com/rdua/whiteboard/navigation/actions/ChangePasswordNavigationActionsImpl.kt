package com.rdua.whiteboard.navigation.actions

import com.rdua.whiteboard.change_password.navigation.ChangePasswordNavigationActions
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import javax.inject.Inject

class ChangePasswordNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator,
) : ChangePasswordNavigationActions {
    override suspend fun navigateBack() {
        appNavigator.navigateBack()
    }
}