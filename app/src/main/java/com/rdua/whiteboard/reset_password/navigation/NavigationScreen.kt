package com.rdua.whiteboard.reset_password.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.reset_password.screen.ResetPasswordScreen

fun NavGraphBuilder.resetPasswordScreen() {
    composable(route = Destination.ResetPasswordScreen.fullRoute) {
        ResetPasswordScreen()
    }
}