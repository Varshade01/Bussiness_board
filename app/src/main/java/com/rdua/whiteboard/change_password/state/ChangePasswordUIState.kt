package com.rdua.whiteboard.change_password.state

data class ChangePasswordUIState(
    val currentPasswordText: String = "",
    val currentPasswordIsError: Boolean = false,
    val currentPasswordErrorTextId: Int? = null,

    val newPasswordText: String = "",
    val newPasswordIsError: Boolean = false,
    val newPasswordErrorTextId: Int? = null,

    val passwordConfirmText: String = "",
    val passwordConfirmIsError: Boolean = false,
    val passwordConfirmErrorTextId: Int? = null,
)