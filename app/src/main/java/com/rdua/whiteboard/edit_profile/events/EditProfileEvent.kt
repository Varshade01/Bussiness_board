package com.rdua.whiteboard.edit_profile.events

import android.graphics.Bitmap
import com.rdua.whiteboard.data.entities.users.Gender

sealed interface EditProfileEvent {
    object NavigateBack : EditProfileEvent
    object NavigateBackDiscardingChanges : EditProfileEvent
    object NavigateBackSavingChanges : EditProfileEvent
    object SaveChanges : EditProfileEvent
    data class NameValueChange(val newValue: String) : EditProfileEvent
    data class EmailValueChange(val newValue: String): EditProfileEvent
    data class MobileValueChange(val newValue: String): EditProfileEvent
    data class GenderValueChange(val newValue: Gender): EditProfileEvent
    class PictureChange(val bitmap: Bitmap) : EditProfileEvent
    object PictureDelete : EditProfileEvent
    object CheckEditIconAction : EditProfileEvent
    object ConfirmPassword : EditProfileEvent
    object DismissConfirmPasswordDialog : EditProfileEvent
    class ConfirmPasswordValueChange(val newValue: String) : EditProfileEvent
}