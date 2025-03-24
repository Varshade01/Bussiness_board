package com.rdua.whiteboard.edit_profile.navigation

import androidx.navigation.NavGraphBuilder
import com.rdua.whiteboard.navigation.destinations.Destination
import androidx.navigation.compose.composable
import com.rdua.whiteboard.edit_profile.screen.EditProfileScreen


fun NavGraphBuilder.editProfileScreen() {
    composable(route = Destination.EditProfileScreen.fullRoute){
        EditProfileScreen()
    }
}