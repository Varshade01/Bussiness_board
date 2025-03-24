package com.rdua.whiteboard.navigation.actions

import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import com.rdua.whiteboard.reset_password.navigation.ResetPasswordNavigationActions
import javax.inject.Inject

class ResetPasswordNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : ResetPasswordNavigationActions {
    override suspend fun navigateToLogin() {
        appNavigator.navigateTo(
            route = Destination.LoginScreen.route,
            popUpToRoute = Destination.ResetPasswordScreen.route,
            inclusive = true,
            isSingleTop = true
        )
    }

    override suspend fun navigateBack() {
        appNavigator.navigateBack()
    }
}