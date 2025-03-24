package com.rdua.whiteboard.settings.navigation

interface ISettingsNavigationActions {

    suspend fun navigateToLogin()

    suspend fun navigateToChangePassword()
}
