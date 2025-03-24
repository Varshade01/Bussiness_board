package com.rdua.whiteboard.navigation.actions

import com.rdua.whiteboard.navigation.destinations.Destination
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import com.rdua.whiteboard.profile_screen_non_edit.navigation.ProfileScreenNavigationActions
import javax.inject.Inject

class ProfileScreenNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : ProfileScreenNavigationActions {

    override suspend fun navigateToProfileEditScreen() {
        appNavigator.navigateTo(
            route = Destination.EditProfileScreen.route,
            isSingleTop = true
        )
    }
}