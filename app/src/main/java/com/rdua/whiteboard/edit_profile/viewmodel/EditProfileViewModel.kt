package com.rdua.whiteboard.edit_profile.viewmodel

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdua.whiteboard.common.manager.toast.ToastEvent
import com.rdua.whiteboard.common.manager.toast.ToastManager
import com.rdua.whiteboard.common.usecases.IGetUserUseCase
import com.rdua.whiteboard.common.validation.mapper.ValidationMapper
import com.rdua.whiteboard.common.validation.usecases.email.ValidateEmailUseCase
import com.rdua.whiteboard.common.validation.usecases.mobile.ValidateMobileUseCase
import com.rdua.whiteboard.common.validation.usecases.name.ValidateNameUseCase
import com.rdua.whiteboard.common.validation.usecases.password.ValidatePasswordUseCase
import com.rdua.whiteboard.data.entities.users.Gender
import com.rdua.whiteboard.data.entities.users.UserEntity
import com.rdua.whiteboard.edit_profile.actions.EditProfileActions
import com.rdua.whiteboard.edit_profile.events.EditProfileEvent
import com.rdua.whiteboard.edit_profile.navigation.EditProfileNavigationActions
import com.rdua.whiteboard.edit_profile.state.EditProfileUIState
import com.rdua.whiteboard.edit_profile.usecases.ChangeAuthEmailUseCase
import com.rdua.whiteboard.edit_profile.usecases.DeleteUserPictureUseCase
import com.rdua.whiteboard.edit_profile.usecases.IUpdateUserUseCase
import com.rdua.whiteboard.edit_profile.usecases.UpdateUserPictureUseCase
import com.rdua.whiteboard.edit_profile.utils.compressBitmap
import com.rdua.whiteboard.firebase.state.ChangeEmailState
import com.rdua.whiteboard.firebase.state.GetUserStateSuccess
import com.rdua.whiteboard.firebase.state.isSuccess
import com.rdua.whiteboard.firebase.utils.toMessageId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.contracts.ExperimentalContracts

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val navigationActions: EditProfileNavigationActions,
    private val getUserUseCase: IGetUserUseCase,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateMobileUseCase: ValidateMobileUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val updateUserUseCase: IUpdateUserUseCase,
    private val updateUserPictureUseCase: UpdateUserPictureUseCase,
    private val deleteUserPictureUseCase: DeleteUserPictureUseCase,
    private val changeAuthEmailUseCase: ChangeAuthEmailUseCase,
    private val toastManager: ToastManager,
) : ViewModel() {
    var state by mutableStateOf(EditProfileUIState())

    private var originUserState: UserEntity? = null
    // Use to remember to navigate back when UI flow goes through series of dialogs.
    private var shouldNavigateBackAfterSave: Boolean = false

    private val _actionFlow = MutableSharedFlow<EditProfileActions>()
    val actionFlow = _actionFlow.asSharedFlow()

    init {
        loadUserData()
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.NavigateBack -> navigateBackCheckingUnsavedChanges()
            is EditProfileEvent.NavigateBackDiscardingChanges -> navigateBackDiscardingChanges()
            is EditProfileEvent.NavigateBackSavingChanges -> navigateBackSavingChanges()
            is EditProfileEvent.EmailValueChange -> updateState(email = event.newValue)
            is EditProfileEvent.GenderValueChange -> updateState(gender = event.newValue)
            is EditProfileEvent.MobileValueChange -> updateState(mobile = event.newValue)
            is EditProfileEvent.NameValueChange -> updateState(name = event.newValue)
            is EditProfileEvent.PictureChange -> changeUserPicture(event.bitmap)
            is EditProfileEvent.PictureDelete -> deleteUserPicture()
            is EditProfileEvent.CheckEditIconAction -> checkEditIconAction()
            is EditProfileEvent.SaveChanges -> saveChanges()
            is EditProfileEvent.ConfirmPassword -> saveChangesWithNewEmail()
            is EditProfileEvent.DismissConfirmPasswordDialog -> dismissConfirmPasswordDialog()
            is EditProfileEvent.ConfirmPasswordValueChange -> updateConfirmPasswordValue(event.newValue)
        }
    }

    /**
     * Saves user data, if all user profiles fields are valid. If email field was edited, shows
     * dialog for password confirmation first.
     */
    private fun saveChanges() {
        if (validateEditProfileFields()) {
            if (isEmailFieldChanged()) {
                showConfirmPasswordDialog()
            } else {
                updateUserData()
            }
        }
    }

    /**
     * Saves user data when email was changed. Email is updated in Authentication service and new user
     * data is saved in the database.
     */
    private fun saveChangesWithNewEmail() {
        if (validateConfirmPassword()) {
            viewModelScope.launch {
                val changeAuthEmailResult = changeAuthEmailUseCase(
                    currentPassword = state.confirmPasswordText,
                    newEmail = state.email
                )

                if (changeAuthEmailResult is ChangeEmailState.Success) {
                    updateUserData()
                    dismissConfirmPasswordDialog()
                } else {
                    state = state.copy(
                        confirmPasswordIsError = true,
                        confirmPasswordErrorTextId = changeAuthEmailResult.toMessageId(),
                    )
                }
            }
        }
    }


    /**
     * Starts saving profile changes flow, setting [shouldNavigateBackAfterSave] to true to navigate
     * back when saving done.
     */
    private fun navigateBackSavingChanges() {
        shouldNavigateBackAfterSave = true
        saveChanges()
    }

    /**
     * If there are unsaved changes, shows the Dialog for unsaved changes. Otherwise just navigates
     * back.
     */
    private fun navigateBackCheckingUnsavedChanges() {
        if (state.isSaveAvailable) {
            state = state.copy(showUnsavedChangesDialog = true)
        } else {
            navigateBack()
        }
    }

    /**
     * Updates user in database. When called from "navigate back flow" and [shouldNavigateBackAfterSave]
     * is `true`, navigates back when user data is updated.
     */
    private fun updateUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            updateUserUseCase.updateUser(
                UserEntity(
                    id = state.userId,
                    name = state.name,
                    email = state.email,
                    photoUrl = state.photoUrl,
                    mobileNumber = state.mobile,
                    gender = state.gender
                )
            )

            if (shouldNavigateBackAfterSave) {
                navigateBack()
            }
        }

        updateOriginUserState()
        state = state.copy(isSaveAvailable = false, showUnsavedChangesDialog = false)
    }

    private fun validateEditProfileFields(): Boolean {
        val nameResult = validateNameUseCase(state.name)
        val nameValidation = ValidationMapper.toValidationState(nameResult)

        val emailResult = validateEmailUseCase(state.email)
        val emailValidation = ValidationMapper.toValidationState(emailResult)

        val mobileResult = validateMobileUseCase(state.mobile)
        val mobileValidation = ValidationMapper.toValidationState(mobileResult)

        state = state.copy(
            nameIsError = nameValidation.isError,
            nameErrorTextId = nameValidation.errorMessageId,
            emailIsError = emailValidation.isError,
            emailErrorTextId = emailValidation.errorMessageId,
            mobileIsError = mobileValidation.isError,
            mobileErrorTextId = mobileValidation.errorMessageId
        )
        return !nameValidation.isError && !emailValidation.isError && !mobileValidation.isError
    }

    private fun validateConfirmPassword(): Boolean {
        val passwordResult = validatePasswordUseCase(state.confirmPasswordText)
        val passwordValidation = ValidationMapper.toValidationState(passwordResult)

        state = state.copy(
            confirmPasswordIsError = passwordValidation.isError,
            confirmPasswordErrorTextId = passwordValidation.errorMessageId,
        )
        return !passwordValidation.isError
    }

    private fun updateState(
        name: String = state.name,
        email: String = state.email,
        mobile: String? = state.mobile,
        gender: Gender = state.gender,
    ) {
        state = state.copy(
            name = name,
            email = email,
            mobile = mobile,
            gender = gender,
        )
        updateSaveButtonAvailability()
    }

    private fun updateConfirmPasswordValue(newValue: String) {
        state = state.copy(confirmPasswordText = newValue)
    }

    private fun updateOriginUserState() {
        originUserState = UserEntity(
            id = state.userId,
            name = state.name,
            email = state.email,
            photoUrl = state.photoUrl,
            mobileNumber = state.mobile,
            gender = state.gender
        )
    }

    private fun updateSaveButtonAvailability() {
        //If there are differences between original user state
        // and current user state, make update button available
        state = state.copy(
            isSaveAvailable = state.name != originUserState?.name
                    || state.email != originUserState?.email
                    || state.mobile != originUserState?.mobileNumber
                    || state.gender != originUserState?.gender
        )
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val getUserStateResult = getUserUseCase.getCurrentUser()
            val messageId = getUserStateResult.toMessageId()
            if (messageId != null) {
                toastManager.sendToast(ToastEvent(messageId, Toast.LENGTH_LONG))
            }

            if (getUserStateResult is GetUserStateSuccess) {
                getUserStateResult.user.let { user ->
                    state = state.copy(
                        userId = user.id,
                        name = user.name,
                        email = user.email,
                        mobile = user.mobileNumber,
                        gender = user.gender,
                        photoUrl = user.photoUrl
                    )
                }
                originUserState = getUserStateResult.user
            }
        }
    }

    @OptIn(ExperimentalContracts::class)
    private fun changeUserPicture(bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = updateUserPictureUseCase(state.userId, compressBitmap(bitmap))
            if (result.isSuccess()) {
                state = state.copy(photoUrl = result.url)
            }
        }
    }

    @OptIn(ExperimentalContracts::class)
    private fun deleteUserPicture() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteUserPictureUseCase(state.userId)
            if (result.isSuccess()){
                state = state.copy(photoUrl = null)
            }
        }
    }

    private fun checkEditIconAction() {
        viewModelScope.launch {
            if (state.photoUrl == null) {
                _actionFlow.emit(EditProfileActions.PickProfilePhoto)
            } else {
                _actionFlow.emit(EditProfileActions.ChangeProfilePhoto)
            }
        }
    }

    private fun showConfirmPasswordDialog() {
        state = state.copy(showConfirmPasswordDialog = true)
    }

    private fun dismissConfirmPasswordDialog() {
        state = state.copy(
            showConfirmPasswordDialog = false,
            confirmPasswordText = "",
            confirmPasswordIsError = false,
            confirmPasswordErrorTextId = null,
        )
    }

    private fun isEmailFieldChanged(): Boolean = state.email != originUserState?.email

    private fun navigateBackDiscardingChanges() {
        // Hiding dialog results in smoother transition, where it doesn't linger on previous screen.
        state = state.copy(showUnsavedChangesDialog = false)
        navigateBack()
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navigationActions.navigateBack()
        }
    }
}