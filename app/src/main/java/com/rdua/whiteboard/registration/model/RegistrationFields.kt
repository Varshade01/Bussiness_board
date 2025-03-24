package com.rdua.whiteboard.registration.model

data class RegistrationFields(
    val name: String,
    val email: String,
    val password: String,
    val temporaryUser: Boolean? = null
)