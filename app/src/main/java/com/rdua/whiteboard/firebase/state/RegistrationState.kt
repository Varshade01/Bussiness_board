package com.rdua.whiteboard.firebase.state

sealed class RegistrationState

class RegistrationSuccess(val userId: String) : RegistrationState()

object RegistrationUserAlreadyExists : RegistrationState()

object RegistrationNetworkFailure : RegistrationState()

object RegistrationUnknownFailure : RegistrationState()



