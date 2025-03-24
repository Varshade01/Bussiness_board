package com.rdua.whiteboard.common.state

data class PasswordState(
    val passwordText: String = "",
    val passwordIsError: Boolean = false,
    val passwordErrorTextId: Int? = null
)