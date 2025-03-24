package com.rdua.whiteboard.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rdua.whiteboard.home.screens.HomeScreen
import com.rdua.whiteboard.navigation.destinations.Destination

fun NavGraphBuilder.homeScreen() {
    composable(route = Destination.HomeScreen.fullRoute) {
        HomeScreen()
    }
}