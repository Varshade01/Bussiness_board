package com.rdua.whiteboard.navigation.actions

import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import com.rdua.whiteboard.splash.navigation.SplashNavigationActions
import javax.inject.Inject

class SplashNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : SplashNavigationActions {
    override suspend fun navigateToLogin() {
        appNavigator.navigateTo(
            route = Destination.LoginScreen.route,
            popUpToRoute = Destination.SplashScreen.route,
            inclusive = true,
            isSingleTop = true
        )
    }

    override suspend fun navigateToHome() {
        appNavigator.navigateTo(
            route = Destination.HomeScreen.route,
            popUpToRoute = Destination.SplashScreen.route,
            inclusive = true,
            isSingleTop = true
        )
    }
}