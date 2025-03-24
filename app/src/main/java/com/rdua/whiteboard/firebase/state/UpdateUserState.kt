package com.rdua.whiteboard.firebase.state

sealed class UpdateUserState

object UpdateUserStateSuccess : UpdateUserState()

object UpdateUserStateDatabaseException : UpdateUserState()

object UpdateUserStateNetworkFailure : UpdateUserState()

object UpdateUserStateRoomNotFound : UpdateUserState()

object UpdateUserStateUnknownFailure : UpdateUserState()