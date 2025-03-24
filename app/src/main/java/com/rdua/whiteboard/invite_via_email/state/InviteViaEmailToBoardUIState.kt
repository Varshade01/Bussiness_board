package com.rdua.whiteboard.invite_via_email.state

data class InviteViaEmailToBoardUIState(
    val email: String = "",
    val isEmailError: Boolean = false,
    val emailErrorTextId: Int? = null,
)
