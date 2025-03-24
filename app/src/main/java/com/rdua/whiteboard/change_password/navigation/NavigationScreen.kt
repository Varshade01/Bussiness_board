package com.rdua.whiteboard.change_password.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rdua.whiteboard.change_password.screen.ChangePasswordScreen
import com.rdua.whiteboard.navigation.destinations.Destination

fun NavGraphBuilder.changePasswordScreen() {
    composable(route = Destination.ChangePassword.fullRoute) {
        ChangePasswordScreen()
    }
}