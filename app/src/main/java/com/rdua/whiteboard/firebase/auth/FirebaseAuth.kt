package com.rdua.whiteboard.firebase.auth

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.rdua.whiteboard.firebase.state.ChangeEmailState
import com.rdua.whiteboard.firebase.state.ChangePasswordState
import com.rdua.whiteboard.firebase.state.DeleteAccountState
import com.rdua.whiteboard.firebase.state.LoginState
import com.rdua.whiteboard.firebase.state.NetworkFailure
import com.rdua.whiteboard.firebase.state.ReauthState
import com.rdua.whiteboard.firebase.state.RegistrationNetworkFailure
import com.rdua.whiteboard.firebase.state.RegistrationState
import com.rdua.whiteboard.firebase.state.RegistrationSuccess
import com.rdua.whiteboard.firebase.state.RegistrationUnknownFailure
import com.rdua.whiteboard.firebase.state.RegistrationUserAlreadyExists
import com.rdua.whiteboard.firebase.state.ReloadState
import com.rdua.whiteboard.firebase.state.ReloadStateError
import com.rdua.whiteboard.firebase.state.ReloadStateFailure
import com.rdua.whiteboard.firebase.state.ReloadStateSuccess
import com.rdua.whiteboard.firebase.state.ResetPasswordState
import com.rdua.whiteboard.firebase.state.ResetPasswordStateSuccess
import com.rdua.whiteboard.firebase.state.UnknownFailure
import com.rdua.whiteboard.firebase.state.UserIsNotExists
import com.rdua.whiteboard.settings.state.LogoutState
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuth @Inject constructor() : FirebaseAuthApi {
    private val firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun register(email: String, password: String): RegistrationState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<AuthResult> { task ->
                val userId = task.result.user?.uid
                if (task.isSuccessful && userId != null) {
                    continuation.resume(RegistrationSuccess(userId = userId))
                } else {
                    when (task.exception) {

                        is FirebaseNetworkException -> {
                            continuation.resume(
                                RegistrationNetworkFailure
                            )
                        }

                        is FirebaseAuthUserCollisionException -> {
                            continuation.resume(
                                RegistrationUserAlreadyExists
                            )
                        }

                        else -> {
                            continuation.resume(RegistrationUnknownFailure)
                        }
                    }
                }
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback)

        }
    }

    override suspend fun login(email: String, password: String): LoginState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    continuation.resume(LoginState.Success)
                } else {
                    when (task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            continuation.resume(LoginState.InvalidCredentials)
                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            continuation.resume(LoginState.InvalidCredentials)
                        }

                        is FirebaseNetworkException -> {
                            continuation.resume(LoginState.NetworkFailure)
                        }

                        else -> {
                            continuation.resume(LoginState.UnknownFailure)
                        }
                    }
                }
            }
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback)
        }
    }

    override suspend fun resetPassword(email: String): ResetPasswordState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<Void> {
                if (it.isSuccessful) {
                    continuation.resume(ResetPasswordStateSuccess)
                } else {
                    when (it.exception) {
                        is FirebaseAuthInvalidUserException ->
                            continuation.resume(UserIsNotExists)

                        is FirebaseNetworkException ->
                            continuation.resume(NetworkFailure)

                        else -> {
                            continuation.resume(UnknownFailure)
                        }
                    }
                }
            }
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(callback)
        }
    }

    override suspend fun changePassword(
        newPassword: String
    ): ChangePasswordState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<Void> {
                if (it.isSuccessful) {
                    continuation.resume(ChangePasswordState.ChangePasswordSuccess)
                } else {
                    when (it.exception) {
                        is FirebaseNetworkException -> {
                            continuation.resume(ChangePasswordState.NetworkFailure)
                        }

                        else -> {
                            continuation.resume(ChangePasswordState.UnknownFailure)
                        }
                    }
                }
            }
            currentUser()?.updatePassword(newPassword)?.addOnCompleteListener(callback)
                ?: continuation.resume(ChangePasswordState.UnknownFailure)
        }
    }

    override suspend fun changeEmail(newEmail: String): ChangeEmailState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<Void> {
                if (it.isSuccessful) {
                    continuation.resume(ChangeEmailState.Success)
                } else {
                    when (it.exception) {
                        is FirebaseNetworkException -> {
                            continuation.resume(ChangeEmailState.NetworkFailure)
                        }

                        is FirebaseAuthUserCollisionException -> {
                            continuation.resume(ChangeEmailState.EmailAlreadyTaken)
                        }

                        else -> {
                            continuation.resume(ChangeEmailState.UnknownFailure)
                        }
                    }
                }
            }
            currentUser()?.updateEmail(newEmail)?.addOnCompleteListener(callback)
                ?: continuation.resume(ChangeEmailState.UnknownFailure)
        }
    }

    override suspend fun reauthenticate(currentPassword: String): ReauthState {
        return suspendCoroutine { continuation ->
            val credential = EmailAuthProvider
                .getCredential(currentUser()?.email!!, currentPassword)

            val callback = OnCompleteListener<Void> {
                if (it.isSuccessful) {
                    continuation.resume(ReauthState.ReauthStateSuccess)
                } else {
                    when (it.exception) {
                        is FirebaseNetworkException -> {
                            continuation.resume(ReauthState.NetworkFailure)
                        }

                        is FirebaseAuthInvalidUserException -> {
                            continuation.resume(ReauthState.UserIsNotExist)
                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            continuation.resume(ReauthState.InvalidCredentials)
                        }

                        else -> {
                            continuation.resume(ReauthState.UnknownFailure)
                        }
                    }
                }
            }
            currentUser()?.reauthenticate(credential)?.addOnCompleteListener(callback)
                ?: continuation.resume(ReauthState.UnknownFailure)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return currentUser() != null
    }

    override suspend fun reload(): ReloadState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<Void> {
                if (it.isSuccessful) {
                    continuation.resume(ReloadStateSuccess)
                } else {
                    when (it.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            continuation.resume(ReloadStateFailure(ReloadStateError.AuthInvalidUserException))
                        }

                        else -> {
                            continuation.resume(ReloadStateFailure(ReloadStateError.UnknownFailure))
                        }
                    }
                }
            }
            currentUser()?.reload()?.addOnCompleteListener(callback) ?: continuation.resume(
                ReloadStateFailure(ReloadStateError.UnknownFailure)
            )
        }
    }

    override fun currentUser(): FirebaseUser? = firebaseAuth.currentUser
    override fun currentUserId(): String? = currentUser()?.uid

    override suspend fun logoutUser(): LogoutState =
        suspendCoroutine { continuation ->
            try {
                firebaseAuth.signOut()
                continuation.resume(LogoutState.LogoutSuccess)
            } catch (exception: Exception) {
                continuation.resume(LogoutState.LogoutFailure)
            }
        }

    override suspend fun deleteAccount(): DeleteAccountState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<Void> {
                if (it.isSuccessful) {
                    continuation.resume(DeleteAccountState.DeleteAccountSuccess)
                } else {
                    when (it.exception) {
                        is FirebaseNetworkException -> {
                            continuation.resume(DeleteAccountState.NetworkFailure)
                        }

                        else -> {
                            continuation.resume(DeleteAccountState.UnknownFailure)
                        }
                    }
                }
            }
            currentUser()?.delete()?.addOnCompleteListener(callback)
                ?: continuation.resume(
                    DeleteAccountState.UnknownFailure
                )
        }
    }
}
