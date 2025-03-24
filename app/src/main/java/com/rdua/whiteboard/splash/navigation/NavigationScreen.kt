package com.rdua.whiteboard.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.splash.screen.SplashScreen

fun NavGraphBuilder.splashScreen() {
    composable(route = Destination.SplashScreen.fullRoute) {
        SplashScreen()
    }
}
