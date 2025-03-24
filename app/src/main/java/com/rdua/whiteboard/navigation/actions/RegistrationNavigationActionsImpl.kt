package com.rdua.whiteboard.navigation.actions

import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import com.rdua.whiteboard.registration.navigation.RegistrationNavigationActions
import javax.inject.Inject

class RegistrationNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : RegistrationNavigationActions {
    override suspend fun navigateToLogin() {
        appNavigator.navigateTo(
            route = Destination.LoginScreen.route,
            popUpToRoute = Destination.RegistrationScreen.route,
            inclusive = true,
            isSingleTop = true
        )
    }

    override suspend fun navigateBack() {
        appNavigator.navigateBack()
    }
}