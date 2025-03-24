package com.rdua.whiteboard.settings.viewmodel

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdua.whiteboard.common.manager.toast.ToastEvent
import com.rdua.whiteboard.common.manager.toast.ToastManager
import com.rdua.whiteboard.common.state.PasswordState
import com.rdua.whiteboard.common.validation.mapper.ValidationMapper
import com.rdua.whiteboard.common.validation.usecases.password.ValidatePasswordUseCase
import com.rdua.whiteboard.data.data_store.SettingsDataStore
import com.rdua.whiteboard.firebase.state.DeleteAccountState
import com.rdua.whiteboard.firebase.utils.toMessageId
import com.rdua.whiteboard.settings.event.SettingsEvent
import com.rdua.whiteboard.settings.navigation.ISettingsNavigationActions
import com.rdua.whiteboard.settings.state.LogoutState
import com.rdua.whiteboard.settings.state.SettingsUIState
import com.rdua.whiteboard.settings.usecase.DeleteAccountUseCase
import com.rdua.whiteboard.settings.usecase.IClearBoardsCacheUseCase
import com.rdua.whiteboard.settings.usecase.ILogoutUseCase
import com.rdua.whiteboard.splash.utils.errorToastId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUseCase: ILogoutUseCase,
    private val clearBoardsCacheUseCase: IClearBoardsCacheUseCase,
    private val navigationActions: ISettingsNavigationActions,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val toastManager: ToastManager,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    var passwordDialogState by mutableStateOf(PasswordState())
        private set

    var state by mutableStateOf(SettingsUIState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(
                isNotificationEnabled = settingsDataStore.isNotificationEnabled()
            )
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ChangePasswordEvent -> navigateToChangePassword()
            is SettingsEvent.OpenDeleteAccountDialogEvent -> state = state.copy(isOpenDeleteDialog = true)
            is SettingsEvent.EnterPasswordEvent -> updateState(password = event.password)
            is SettingsEvent.OpenLogoutDialogEvent -> state = state.copy(isOpenLogoutDialog = true)
            is SettingsEvent.LogoutEvent -> logout()
            is SettingsEvent.DismissDeleteDialogEvent -> closeDeleteDialog()
            is SettingsEvent.DismissLogoutDialogEvent -> state = state.copy(isOpenLogoutDialog = false)
            is SettingsEvent.DeleteAccountDialogEvent -> processAccountDeletion()
            is SettingsEvent.PushNotificationEvent -> updateNotificationState(event.isChecked)
        }
    }

    private fun logout() {
        state = state.copy(isOpenLogoutDialog = false)

        viewModelScope.launch {
            when (logoutUseCase.logoutUser()) {
                is LogoutState.LogoutSuccess -> {
                    clearBoardsCacheUseCase.clearBoardsCache()
                    navigateToLogin()
                }

                is LogoutState.LogoutFailure -> {
                    toastManager.sendToast(ToastEvent(errorToastId, Toast.LENGTH_SHORT))
                }
            }
        }
    }

    private fun processAccountDeletion() {
        if (validatePassword()) {
            state = state.copy(isOpenDeleteDialog = false)
            executeAccountDeletion(passwordDialogState.passwordText)
        }
    }

    private fun executeAccountDeletion(password: String) {
        viewModelScope.launch {
            state = state.copy(showProgressDeleteAccount = true)

            val deleteAccountResult = deleteAccountUseCase.deleteAccount(password)
            val messageId = deleteAccountResult.toMessageId()
            toastManager.sendToast(ToastEvent(messageId, Toast.LENGTH_LONG))

            if (deleteAccountResult is DeleteAccountState.DeleteAccountSuccess) {
                clearBoardsCacheUseCase.clearBoardsCache()
                navigateToLogin()
            }
            state = state.copy(showProgressDeleteAccount = false)
        }
    }

    private fun updateState(
        password: String = passwordDialogState.passwordText
    ) {
        passwordDialogState = passwordDialogState.copy(
            passwordText = password
        )
    }

    private fun validatePassword(): Boolean {
        val passwordResult = validatePasswordUseCase(passwordDialogState.passwordText)
        val passwordValidation = ValidationMapper.toValidationState(passwordResult)

        passwordDialogState = passwordDialogState.copy(
            passwordIsError = passwordValidation.isError,
            passwordErrorTextId = passwordValidation.errorMessageId,
        )
        return !passwordValidation.isError
    }

    private fun updateNotificationState(isChecked: Boolean) {
        viewModelScope.launch {
            settingsDataStore.saveNotificationEnabled(isChecked)
            state = state.copy(isNotificationEnabled = isChecked)
        }
    }

    private fun closeDeleteDialog() {
        state = state.copy(isOpenDeleteDialog = false)
        passwordDialogState = PasswordState()
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            navigationActions.navigateToLogin()
        }
    }

    private fun navigateToChangePassword() {
        viewModelScope.launch {
            navigationActions.navigateToChangePassword()
        }
    }
}
