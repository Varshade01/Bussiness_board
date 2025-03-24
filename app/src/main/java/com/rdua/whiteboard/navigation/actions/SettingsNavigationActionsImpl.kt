package com.rdua.whiteboard.navigation.actions

import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import com.rdua.whiteboard.settings.navigation.ISettingsNavigationActions
import javax.inject.Inject

class SettingsNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator,
) : ISettingsNavigationActions {

    override suspend fun navigateToLogin() {
        appNavigator.navigateTo(
            route = Destination.LoginScreen.route,
            popUpToRoute = Destination.HomeScreen.route,
            inclusive = true
        )
    }

    override suspend fun navigateToChangePassword() {
        appNavigator.navigateTo(
            route = Destination.ChangePassword.route,
            popUpToRoute = Destination.Settings.route,
            inclusive = true
        )
    }
}
