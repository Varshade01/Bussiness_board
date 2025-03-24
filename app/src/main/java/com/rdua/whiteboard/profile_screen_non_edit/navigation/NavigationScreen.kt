package com.rdua.whiteboard.profile_screen_non_edit.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.profile_screen_non_edit.screen.ProfileScreen

fun NavGraphBuilder.profileScreen() {
    composable(route = Destination.ProfileScreen.fullRoute) {
        ProfileScreen()
    }
}