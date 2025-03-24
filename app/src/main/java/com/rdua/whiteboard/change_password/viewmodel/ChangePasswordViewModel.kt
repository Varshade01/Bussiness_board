package com.rdua.whiteboard.change_password.viewmodel

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdua.whiteboard.change_password.events.ChangePasswordEvent
import com.rdua.whiteboard.change_password.navigation.ChangePasswordNavigationActions
import com.rdua.whiteboard.change_password.state.ChangePasswordUIState
import com.rdua.whiteboard.change_password.usecase.ChangePasswordUseCase
import com.rdua.whiteboard.common.manager.toast.ToastEvent
import com.rdua.whiteboard.common.manager.toast.ToastManager
import com.rdua.whiteboard.common.validation.mapper.ValidationMapper
import com.rdua.whiteboard.common.validation.usecases.confirm.ValidateConfirmPasswordUseCase
import com.rdua.whiteboard.common.validation.usecases.password.ValidateChangePasswordUseCaseImpl
import com.rdua.whiteboard.common.validation.usecases.password.ValidatePasswordUseCase
import com.rdua.whiteboard.firebase.state.ChangePasswordState
import com.rdua.whiteboard.firebase.utils.toMessageId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val navigationActions: ChangePasswordNavigationActions,
    private val toastManager: ToastManager,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateChangePasswordUseCase: ValidateChangePasswordUseCaseImpl,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
) : ViewModel() {

    var state by mutableStateOf(ChangePasswordUIState())

    fun onEvent(event: ChangePasswordEvent) {
        when (event) {
            is ChangePasswordEvent.CurrentPasswordChange -> {
                updateState(currentPasswordText = event.newValue)
            }

            is ChangePasswordEvent.NewPasswordChange -> {
                updateState(newPasswordText = event.newValue)
            }

            is ChangePasswordEvent.PasswordConfirmChange -> {
                updateState(passwordConfirmText = event.newValue)
            }

            is ChangePasswordEvent.ChangePassword -> changePassword()

            is ChangePasswordEvent.NavigateBack -> navigateBack()
        }
    }

    private fun updateState(
        currentPasswordText: String = state.currentPasswordText,
        newPasswordText: String = state.newPasswordText,
        passwordConfirmText: String = state.passwordConfirmText
    ) {
        state = state.copy(
            currentPasswordText = currentPasswordText,
            newPasswordText = newPasswordText,
            passwordConfirmText = passwordConfirmText
        )
    }

    private fun validateFields(): Boolean {
        val currentPasswordResult = validatePasswordUseCase(state.currentPasswordText)
        val currentPasswordValidation = ValidationMapper.toValidationState(currentPasswordResult)

        val newPasswordResult = validateChangePasswordUseCase(state.currentPasswordText, state.newPasswordText)
        val newPasswordValidation = ValidationMapper.toValidationState(newPasswordResult)

        val passwordConfirmResult = validateConfirmPasswordUseCase(
            state.newPasswordText,
            state.passwordConfirmText
        )
        val passwordConfirmValidation = ValidationMapper.toValidationState(passwordConfirmResult)

        state = state.copy(
            currentPasswordIsError = currentPasswordValidation.isError,
            currentPasswordErrorTextId = currentPasswordValidation.errorMessageId,

            newPasswordIsError = newPasswordValidation.isError,
            newPasswordErrorTextId = newPasswordValidation.errorMessageId,

            passwordConfirmIsError = passwordConfirmValidation.isError,
            passwordConfirmErrorTextId = passwordConfirmValidation.errorMessageId
        )

        return !currentPasswordValidation.isError && !newPasswordValidation.isError &&
                !passwordConfirmValidation.isError
    }



    private fun changePassword() {
        if (validateFields()) {
            viewModelScope.launch {
                val currentPassword = state.currentPasswordText
                val newPassword = state.passwordConfirmText

                val changePasswordResult = changePasswordUseCase.changePassword(
                    currentPassword, newPassword
                )

                val messageId = changePasswordResult.toMessageId()
                if (changePasswordResult is ChangePasswordState.InvalidCredentials) {
                    state = state.copy(
                        currentPasswordIsError = true,
                        currentPasswordErrorTextId = messageId,
                    )
                } else {
                    toastManager.sendToast(ToastEvent(messageId, Toast.LENGTH_LONG))
                }

                if (changePasswordResult is ChangePasswordState.ChangePasswordSuccess) {
                    navigateBack()
                }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navigationActions.navigateBack()
        }
    }

}
