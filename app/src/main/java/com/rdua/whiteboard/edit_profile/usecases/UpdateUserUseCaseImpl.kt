package com.rdua.whiteboard.edit_profile.usecases

import com.rdua.whiteboard.data.entities.users.UserEntity
import com.rdua.whiteboard.firebase.state.UpdateUserState
import com.rdua.whiteboard.repository.users.UsersRepository
import javax.inject.Inject

class UpdateUserUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository,
): IUpdateUserUseCase{
    override suspend fun updateUser(
        user: UserEntity
    ): UpdateUserState {
        return usersRepository.updateUser(user)
    }

}