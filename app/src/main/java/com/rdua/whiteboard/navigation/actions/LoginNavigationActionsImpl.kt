package com.rdua.whiteboard.navigation.actions

import com.rdua.whiteboard.login.navigation.LoginNavigationActions
import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import javax.inject.Inject

class LoginNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : LoginNavigationActions {
    override suspend fun navigateToRegistration() {
        appNavigator.navigateTo(
            route = Destination.RegistrationScreen.route,
            isSingleTop = true
        )
    }

    override suspend fun navigateToResetPassword() {
        appNavigator.navigateTo(
            route = Destination.ResetPasswordScreen.route,
            isSingleTop = true
        )
    }

    override suspend fun navigateToHome() {
        appNavigator.navigateTo(
            route = Destination.HomeScreen.route,
            popUpToRoute = Destination.LoginScreen.route,
            inclusive = true,
            isSingleTop = true
        )
    }
}