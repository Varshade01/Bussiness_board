package com.rdua.whiteboard.reset_password.viewmodel

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdua.whiteboard.common.manager.toast.ToastEvent
import com.rdua.whiteboard.common.manager.toast.ToastManager
import com.rdua.whiteboard.common.validation.mapper.ValidationMapper
import com.rdua.whiteboard.common.validation.usecases.email.ValidateEmailUseCase
import com.rdua.whiteboard.firebase.state.ResetPasswordStateSuccess
import com.rdua.whiteboard.firebase.utils.toMessageId
import com.rdua.whiteboard.reset_password.event.ResetPasswordEvent
import com.rdua.whiteboard.reset_password.navigation.ResetPasswordNavigationActions
import com.rdua.whiteboard.reset_password.state.ResetPasswordUIState
import com.rdua.whiteboard.reset_password.usecases.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val navigationActions: ResetPasswordNavigationActions,
    private val toastManager: ToastManager,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    var state by mutableStateOf(ResetPasswordUIState())

    fun onEvent(event: ResetPasswordEvent) {
        when (event) {
            is ResetPasswordEvent.EmailChange -> updateState(emailText = event.newValue)
            is ResetPasswordEvent.ResetPassword -> resetPassword()
            is ResetPasswordEvent.NavigateToLogin -> navigateToLogin()
            is ResetPasswordEvent.NavigateBack -> navigateBack()
        }
    }

    private fun updateState(
        emailText: String = state.emailText,
    ) {
        state = state.copy(
            emailText = emailText,
        )
    }

    private fun resetPassword() {
        viewModelScope.launch {
            if (validateEmail()) {
                val email = state.emailText
                val resetPasswordStateResult = resetPasswordUseCase.resetPassword(email)

                toastManager.sendToast(ToastEvent(resetPasswordStateResult.toMessageId(), Toast.LENGTH_LONG))

                if (resetPasswordStateResult is ResetPasswordStateSuccess) {
                    navigateToLogin()
                }
            }
        }
    }

    private fun validateEmail(): Boolean {
        val emailResult = validateEmailUseCase(state.emailText)
        val emailValidation = ValidationMapper.toValidationState(emailResult)

        state = state.copy(
            emailIsError = emailValidation.isError,
            emailErrorTextId = emailValidation.errorMessageId,
        )
        return !emailValidation.isError
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            navigationActions.navigateToLogin()
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navigationActions.navigateBack()
        }
    }
}