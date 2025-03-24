package com.rdua.whiteboard.splash.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rdua.whiteboard.common.manager.toast.ToastEvent
import com.rdua.whiteboard.common.manager.toast.ToastManager
import com.rdua.whiteboard.firebase.auth.FirebaseAuthApi
import com.rdua.whiteboard.firebase.state.ReloadStateFailure
import com.rdua.whiteboard.firebase.state.ReloadStateSuccess
import com.rdua.whiteboard.firebase.connection.FirebaseConnection
import com.rdua.whiteboard.splash.navigation.SplashNavigationActions
import com.rdua.whiteboard.splash.utils.errorToastId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val navigationActions: SplashNavigationActions,
    private val toastManager: ToastManager,
    private val firebaseConnectionImpl: FirebaseConnection,
    private val firebaseAuth: FirebaseAuthApi
) : ViewModel() {

    init {
        checkConnection()
    }

    private fun checkConnection() {
        viewModelScope.launch(Dispatchers.Default) {
            if (firebaseConnectionImpl.waitConnection())
                checkUserLoggedIn()
            else
                toastManager.sendToast(ToastEvent(errorToastId, Toast.LENGTH_SHORT))
        }
    }

    private fun checkUserLoggedIn() {
        viewModelScope.launch(Dispatchers.Default) {
            val isUserLoggedIn = firebaseAuth.isUserLoggedIn()
            if (!isUserLoggedIn) {
                navigateToLogin()
            } else {
                when (firebaseAuth.reload()) {
                    is ReloadStateSuccess -> navigateToHome()
                    is ReloadStateFailure -> navigateToLogin()
                }
            }
        }
    }

    private fun navigateToHome() {
        viewModelScope.launch {
            navigationActions.navigateToHome()
        }
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            navigationActions.navigateToLogin()
        }
    }
}