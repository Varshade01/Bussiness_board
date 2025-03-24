package com.rdua.whiteboard.reset_password.navigation

interface ResetPasswordNavigationActions {

    suspend fun navigateToLogin()

    suspend fun navigateBack()
}