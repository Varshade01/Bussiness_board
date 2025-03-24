package com.rdua.whiteboard.login.viewmodel

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
import com.rdua.whiteboard.common.validation.usecases.password.ValidatePasswordUseCase
import com.rdua.whiteboard.firebase.state.LoginState
import com.rdua.whiteboard.firebase.utils.toMessageId
import com.rdua.whiteboard.login.events.LoginEvent
import com.rdua.whiteboard.login.model.LoginFields
import com.rdua.whiteboard.login.navigation.LoginNavigationActions
import com.rdua.whiteboard.login.state.LoginUIState
import com.rdua.whiteboard.login.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navigationActions: LoginNavigationActions,
    private val toastManager: ToastManager,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    var state by mutableStateOf(LoginUIState())

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChange -> updateState(emailText = event.newValue)
            is LoginEvent.PasswordChange -> updateState(passwordText = event.newValue)
            is LoginEvent.Login -> login()
            is LoginEvent.NavigateToRegister -> navigateToRegistration()
            is LoginEvent.NavigateToResetPassword -> navigateToResetPassword()
        }
    }

    private fun updateState(
        emailText: String = state.emailText,
        passwordText: String = state.passwordText,
    ) {
        state = state.copy(
            emailText = emailText,
            passwordText = passwordText,
        )
    }

    private fun login() {
        if (validateLoginFields()) {
            viewModelScope.launch(Dispatchers.IO) {
                val fields = LoginFields(
                    email = state.emailText,
                    password = state.passwordText
                )

                val loginResult = loginUseCase(fields)
                val messageId = loginResult.toMessageId()
                if (messageId != null) {
                    toastManager.sendToast(ToastEvent(messageId, Toast.LENGTH_LONG))
                }

                if (loginResult is LoginState.Success) {
                    navigateToHome()
                }
            }
        }
    }

    private fun validateLoginFields(): Boolean {
        val emailResult = validateEmailUseCase(state.emailText)
        val emailValidation = ValidationMapper.toValidationState(emailResult)

        val passwordResult = validatePasswordUseCase(state.passwordText)
        val passwordValidation = ValidationMapper.toValidationState(passwordResult)

        state = state.copy(
            emailIsError = emailValidation.isError,
            emailErrorTextId = emailValidation.errorMessageId,
            passwordIsError = passwordValidation.isError,
            passwordErrorTextId = passwordValidation.errorMessageId,
        )

        return !emailValidation.isError && !passwordValidation.isError
    }

    private fun navigateToRegistration() {
        viewModelScope.launch {
            navigationActions.navigateToRegistration()
        }
    }

    private fun navigateToResetPassword() {
        viewModelScope.launch {
            navigationActions.navigateToResetPassword()
        }
    }

    private fun navigateToHome() {
        viewModelScope.launch {
            navigationActions.navigateToHome()
        }
    }
}