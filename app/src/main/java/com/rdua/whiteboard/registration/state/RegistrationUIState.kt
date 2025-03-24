package com.rdua.whiteboard.registration.state

data class RegistrationUIState(
    val nameText: String = "",
    val nameIsError: Boolean = false,
    val nameErrorTextId: Int? = null,
    val emailText: String = "",
    val emailIsError: Boolean = false,
    val emailErrorTextId: Int? = null,
    val passwordText: String = "",
    val passwordIsError: Boolean = false,
    val passwordErrorTextId: Int? = null,
    val passwordConfirmText: String = "",
    val passwordConfirmIsError: Boolean = false,
    val passwordConfirmErrorTextId: Int? = null,
)