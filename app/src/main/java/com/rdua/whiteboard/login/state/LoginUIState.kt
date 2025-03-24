package com.rdua.whiteboard.login.state

data class LoginUIState(
    val emailText: String = "",
    val emailIsError: Boolean = false,
    val emailErrorTextId: Int? = null,
    val passwordText: String = "",
    val passwordIsError: Boolean = false,
    val passwordErrorTextId: Int? = null
)