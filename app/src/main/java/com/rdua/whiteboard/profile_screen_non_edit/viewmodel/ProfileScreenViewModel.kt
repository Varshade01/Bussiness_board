package com.rdua.whiteboard.profile_screen_non_edit.viewmodel

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdua.whiteboard.common.manager.toast.ToastEvent
import com.rdua.whiteboard.common.manager.toast.ToastManager
import com.rdua.whiteboard.common.usecases.IGetUserUseCase
import com.rdua.whiteboard.data.entities.users.UserMapper
import com.rdua.whiteboard.firebase.state.GetUserStateSuccess
import com.rdua.whiteboard.firebase.utils.toMessageId
import com.rdua.whiteboard.profile_screen_non_edit.events.ProfileScreenEvent
import com.rdua.whiteboard.profile_screen_non_edit.navigation.ProfileScreenNavigationActions
import com.rdua.whiteboard.profile_screen_non_edit.state.UserUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val navigationActions: ProfileScreenNavigationActions,
    private val toastManager: ToastManager,
    private val getUserUseCase: IGetUserUseCase,
) : ViewModel() {

    var user by mutableStateOf(UserUI())

    fun onEvent(event: ProfileScreenEvent) {
        when (event) {
            is ProfileScreenEvent.NavigateToProfileEditScreen -> {
                navigateToEditScreen()
            }
            is ProfileScreenEvent.LoadUser -> {
                getUser()
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            val userStateResult = getUserUseCase.getCurrentUser()
            val messageId = userStateResult.toMessageId()
            if (messageId != null) {
                toastManager.sendToast(ToastEvent(messageId, Toast.LENGTH_LONG))
            }

            if (userStateResult is GetUserStateSuccess) {
                user = UserMapper.toUserUI(userStateResult.user)
            }
        }
    }

    private fun navigateToEditScreen() {
        viewModelScope.launch {
            navigationActions.navigateToProfileEditScreen()
        }
    }
}