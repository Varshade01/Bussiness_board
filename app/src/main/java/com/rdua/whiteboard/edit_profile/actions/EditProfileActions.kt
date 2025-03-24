package com.rdua.whiteboard.edit_profile.actions

sealed interface EditProfileActions {
    object PickProfilePhoto: EditProfileActions
    object ChangeProfilePhoto: EditProfileActions
}