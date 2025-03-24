package com.rdua.whiteboard.firebase.utils

import com.rdua.whiteboard.R
import com.rdua.whiteboard.firebase.state.ChangeEmailState
import com.rdua.whiteboard.firebase.state.ChangePasswordState
import com.rdua.whiteboard.firebase.state.DeleteAccountState
import com.rdua.whiteboard.firebase.state.GetUserState
import com.rdua.whiteboard.firebase.state.GetUserStateDatabaseException
import com.rdua.whiteboard.firebase.state.GetUserStateSuccess
import com.rdua.whiteboard.firebase.state.LoginState
import com.rdua.whiteboard.firebase.state.NetworkFailure
import com.rdua.whiteboard.firebase.state.RegistrationNetworkFailure
import com.rdua.whiteboard.firebase.state.RegistrationState
import com.rdua.whiteboard.firebase.state.RegistrationSuccess
import com.rdua.whiteboard.firebase.state.RegistrationUnknownFailure
import com.rdua.whiteboard.firebase.state.RegistrationUserAlreadyExists
import com.rdua.whiteboard.firebase.state.ResetPasswordState
import com.rdua.whiteboard.firebase.state.ResetPasswordStateSuccess
import com.rdua.whiteboard.firebase.state.UnknownFailure
import com.rdua.whiteboard.firebase.state.UpdateUserState
import com.rdua.whiteboard.firebase.state.UpdateUserStateDatabaseException
import com.rdua.whiteboard.firebase.state.UpdateUserStateNetworkFailure
import com.rdua.whiteboard.firebase.state.UpdateUserStateSuccess
import com.rdua.whiteboard.firebase.state.UpdateUserStateUnknownFailure
import com.rdua.whiteboard.firebase.state.UserIsNotExists
import com.rdua.whiteboard.rename_board.state.RenameBoardState

fun RegistrationState.toMessageId(): Int = when (this) {
    is RegistrationUserAlreadyExists -> R.string.user_with_this_email_already_exists
    is RegistrationNetworkFailure -> R.string.no_internet_connection
    is RegistrationUnknownFailure -> R.string.unknown_error_has_occurred
    is RegistrationSuccess -> R.string.registration_success
}

fun LoginState.toMessageId(): Int? = when (this) {
    is LoginState.InvalidCredentials -> R.string.invalid_login_credentials
    is LoginState.NetworkFailure -> R.string.no_internet_connection
    is LoginState.UnknownFailure -> R.string.unknown_error_has_occurred
    is LoginState.Success -> null
}

fun ResetPasswordState.toMessageId(): Int = when (this) {
    is UserIsNotExists -> R.string.user_is_not_exists_message
    is NetworkFailure -> R.string.no_internet_connection
    is UnknownFailure -> R.string.unknown_error_has_occurred
    is ResetPasswordStateSuccess -> R.string.reset_password_message
}

fun GetUserState.toMessageId(): Int? = when (this) {
    is GetUserStateSuccess -> null
    is GetUserStateDatabaseException -> R.string.try_later
    else -> R.string.unknown_error_has_occurred
}

fun ChangePasswordState.toMessageId(): Int = when (this) {
    is ChangePasswordState.UserIsNotExists -> R.string.user_is_not_exists_message
    is ChangePasswordState.InvalidCredentials -> R.string.invalid_login_password
    is ChangePasswordState.ChangePasswordSuccess -> R.string.change_password_success
    is ChangePasswordState.NetworkFailure -> R.string.no_internet_connection
    else -> R.string.unknown_error_has_occurred
}

fun ChangeEmailState.toMessageId(): Int? = when (this) {
    is ChangeEmailState.Success -> null
    is ChangeEmailState.UserIsNotExists -> R.string.user_is_not_exists_message
    is ChangeEmailState.EmailAlreadyTaken -> R.string.user_with_this_email_already_exists
    is ChangeEmailState.InvalidCredentials -> R.string.invalid_login_password
    is ChangeEmailState.NetworkFailure -> R.string.no_internet_connection
    is ChangeEmailState.UnknownFailure -> R.string.unknown_error_has_occurred
}

fun UpdateUserState.toMessageId(): Int? = when (this) {
    is UpdateUserStateSuccess -> null
    is UpdateUserStateNetworkFailure -> R.string.no_internet_connection
    is UpdateUserStateDatabaseException -> R.string.try_later
    is UpdateUserStateUnknownFailure -> R.string.unknown_error_has_occurred
    else -> R.string.unknown_error_has_occurred
}

fun DeleteAccountState.toMessageId(): Int = when (this) {
    is DeleteAccountState.UserIsNotExist -> R.string.user_is_not_exists_message
    is DeleteAccountState.InvalidCredentials -> R.string.invalid_login_credentials
    is DeleteAccountState.DeleteAccountSuccess -> R.string.delete_account_success
    is DeleteAccountState.NetworkFailure -> R.string.no_internet_connection
    else -> R.string.unknown_error_has_occurred
}

fun RenameBoardState.toMessageId(): Int? = when (this) {
    is RenameBoardState.RenameBoardFailure -> R.string.unknown_error_has_occurred
    is RenameBoardState.RenameBoardSuccess -> null
}
