package com.rdua.whiteboard.edit_profile.usecases

import com.rdua.whiteboard.data.entities.users.UserEntity
import com.rdua.whiteboard.firebase.state.UpdateUserState

interface IUpdateUserUseCase {
    suspend fun updateUser(user: UserEntity): UpdateUserState
}