package com.rdua.whiteboard.registration.navigation

interface RegistrationNavigationActions {
    suspend fun navigateToLogin()
    suspend fun navigateBack()
}