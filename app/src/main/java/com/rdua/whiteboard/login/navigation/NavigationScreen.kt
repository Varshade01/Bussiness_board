package com.rdua.whiteboard.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rdua.whiteboard.login.screen.LoginScreen
import com.rdua.whiteboard.navigation.destinations.Destination

fun NavGraphBuilder.loginScreen() {
    composable(route = Destination.LoginScreen.fullRoute) {
        LoginScreen()
    }
}