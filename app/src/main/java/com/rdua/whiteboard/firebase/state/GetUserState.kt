package com.rdua.whiteboard.firebase.state

import com.rdua.whiteboard.data.entities.users.UserEntity

sealed class GetUserState

data class GetUserStateSuccess(val user: UserEntity) : GetUserState()

object GetUserStateDatabaseException : GetUserState()

object GetUserStateRoomNotFound : GetUserState()
object GetUserStateUserNotFound : GetUserState()

object GetUserStateUnknownFailure : GetUserState()