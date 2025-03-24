package com.rdua.whiteboard.login.navigation

interface LoginNavigationActions {
    suspend fun navigateToRegistration()
    suspend fun navigateToResetPassword()
    suspend fun navigateToHome()
}