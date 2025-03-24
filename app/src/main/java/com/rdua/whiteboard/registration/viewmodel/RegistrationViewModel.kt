package com.rdua.whiteboard.registration.viewmodel

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdua.whiteboard.common.manager.toast.ToastEvent
import com.rdua.whiteboard.common.manager.toast.ToastManager
import com.rdua.whiteboard.common.validation.mapper.ValidationMapper
import com.rdua.whiteboard.common.validation.usecases.confirm.ValidateConfirmPasswordUseCase
import com.rdua.whiteboard.common.validation.usecases.email.ValidateEmailUseCase
import com.rdua.whiteboard.common.validation.usecases.name.ValidateNameUseCase
import com.rdua.whiteboard.common.validation.usecases.password.ValidatePasswordUseCase
import com.rdua.whiteboard.firebase.state.RegistrationSuccess
import com.rdua.whiteboard.firebase.utils.toMessageId
import com.rdua.whiteboard.registration.events.RegistrationEvent
import com.rdua.whiteboard.registration.model.RegistrationFields
import com.rdua.whiteboard.registration.navigation.RegistrationNavigationActions
import com.rdua.whiteboard.registration.state.RegistrationUIState
import com.rdua.whiteboard.registration.usecases.ICreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val navigationActions: RegistrationNavigationActions,
    private val toastManager: ToastManager,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
    private val createUserUseCase: ICreateUserUseCase
) : ViewModel() {
    var state by mutableStateOf(RegistrationUIState())

    fun onEvent(event: RegistrationEvent) = when (event) {
        is RegistrationEvent.NameChange -> updateState(nameText = event.newValue)
        is RegistrationEvent.EmailChange -> updateState(emailText = event.newValue)
        is RegistrationEvent.PasswordChange -> updateState(passwordText = event.newValue)
        is RegistrationEvent.PasswordConfirmChange -> updateState(passwordConfirmText = event.newValue)
        is RegistrationEvent.Register -> register()
        is RegistrationEvent.NavigateToLogin -> navigateToLoginScreen()
        is RegistrationEvent.NavigateBack -> navigateBack()
    }

    private fun updateState(
        nameText: String = state.nameText,
        emailText: String = state.emailText,
        passwordText: String = state.passwordText,
        passwordConfirmText: String = state.passwordConfirmText,
    ) {
        state = state.copy(
            nameText = nameText,
            emailText = emailText,
            passwordText = passwordText,
            passwordConfirmText = passwordConfirmText
        )
    }

    private fun register() {
        if (validateRegistrationFields()) {
            viewModelScope.launch(Dispatchers.Default) {
                val fields = RegistrationFields(
                    name = state.nameText,
                    email = state.emailText,
                    password = state.passwordText
                )

                val registrationResult = createUserUseCase.createUser(fields)
                toastManager.sendToast(ToastEvent(registrationResult.toMessageId(), Toast.LENGTH_LONG))

                if (registrationResult is RegistrationSuccess) {
                    navigateToLoginScreen()
                }
            }
        }
    }

    private fun validateRegistrationFields(): Boolean {
        val nameResult = validateNameUseCase(state.nameText)
        val nameValidation = ValidationMapper.toValidationState(nameResult)

        val emailResult = validateEmailUseCase(state.emailText)
        val emailValidation = ValidationMapper.toValidationState(emailResult)

        val passwordResult = validatePasswordUseCase(state.passwordText)
        val passwordValidation = ValidationMapper.toValidationState(passwordResult)

        val passwordConfirmResult = validateConfirmPasswordUseCase(
            state.passwordText,
            state.passwordConfirmText
        )
        val passwordConfirmValidation = ValidationMapper.toValidationState(passwordConfirmResult)

        state = state.copy(
            nameIsError = nameValidation.isError,
            nameErrorTextId = nameValidation.errorMessageId,
            emailIsError = emailValidation.isError,
            emailErrorTextId = emailValidation.errorMessageId,
            passwordIsError = passwordValidation.isError,
            passwordErrorTextId = passwordValidation.errorMessageId,
            passwordConfirmIsError = passwordConfirmValidation.isError,
            passwordConfirmErrorTextId = passwordConfirmValidation.errorMessageId
        )

        return !nameValidation.isError &&
                !emailValidation.isError &&
                !passwordValidation.isError &&
                !passwordConfirmValidation.isError
    }

    private fun navigateToLoginScreen() {
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