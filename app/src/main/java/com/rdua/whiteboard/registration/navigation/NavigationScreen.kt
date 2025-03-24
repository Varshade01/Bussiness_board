package com.rdua.whiteboard.registration.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.registration.screen.RegistrationScreen

fun NavGraphBuilder.registrationScreen() {
    composable(route = Destination.RegistrationScreen.fullRoute) {
        RegistrationScreen()
    }
}