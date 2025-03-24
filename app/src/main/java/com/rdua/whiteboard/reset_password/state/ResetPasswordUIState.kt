package com.rdua.whiteboard.reset_password.state

data class ResetPasswordUIState(
    val emailText: String = "",
    val emailIsError: Boolean = false,
    val emailErrorTextId: Int? = null
)