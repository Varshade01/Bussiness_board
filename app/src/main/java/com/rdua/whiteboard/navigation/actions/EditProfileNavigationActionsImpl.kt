package com.rdua.whiteboard.navigation.actions

import com.rdua.whiteboard.edit_profile.navigation.EditProfileNavigationActions
import com.rdua.whiteboard.navigation.navigator.AppNavigator
import javax.inject.Inject

class EditProfileNavigationActionsImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : EditProfileNavigationActions {

    override suspend fun navigateBack() {
        appNavigator.navigateBack()
    }
}