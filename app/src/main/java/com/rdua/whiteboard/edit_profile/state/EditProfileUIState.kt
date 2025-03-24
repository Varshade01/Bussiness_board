package com.rdua.whiteboard.edit_profile.state

import com.rdua.whiteboard.data.entities.users.Gender

data class EditProfileUIState(
    val userId: String = "",
    val name: String = "",
    val nameIsError:Boolean = false,
    val nameErrorTextId: Int? = null,
    val email: String = "",
    val emailIsError: Boolean = false,
    val emailErrorTextId: Int? = null,
    val mobile: String? = null,
    val mobileIsError: Boolean = false,
    val mobileErrorTextId: Int? = null,
    val gender: Gender = Gender.PREFER_NOT_TO_SAY,
    val photoUrl: String? = null,
    val confirmPasswordText: String = "",
    val confirmPasswordIsError: Boolean = false,
    val confirmPasswordErrorTextId: Int? = null,
    val isSaveAvailable: Boolean = false,
    val showUnsavedChangesDialog: Boolean = false,
    val showConfirmPasswordDialog: Boolean = false,
)