package com.rdua.whiteboard.firebase.auth

import com.google.firebase.auth.FirebaseUser
import com.rdua.whiteboard.firebase.state.ChangeEmailState
import com.rdua.whiteboard.firebase.state.ChangePasswordState
import com.rdua.whiteboard.firebase.state.DeleteAccountState
import com.rdua.whiteboard.firebase.state.LoginState
import com.rdua.whiteboard.firebase.state.ReauthState
import com.rdua.whiteboard.firebase.state.RegistrationState
import com.rdua.whiteboard.firebase.state.ReloadState
import com.rdua.whiteboard.firebase.state.ResetPasswordState
import com.rdua.whiteboard.settings.state.LogoutState

interface FirebaseAuthApi {
    suspend fun register(email: String, password: String): RegistrationState
    suspend fun login(email: String, password: String): LoginState
    suspend fun resetPassword(email: String): ResetPasswordState
    suspend fun changePassword(newPassword: String): ChangePasswordState
    suspend fun changeEmail(newEmail: String): ChangeEmailState
    suspend fun reauthenticate(currentPassword: String): ReauthState
    suspend fun deleteAccount(): DeleteAccountState
    suspend fun reload(): ReloadState
    fun isUserLoggedIn(): Boolean
    fun currentUser(): FirebaseUser?
    fun currentUserId(): String?
    suspend fun logoutUser(): LogoutState
}
