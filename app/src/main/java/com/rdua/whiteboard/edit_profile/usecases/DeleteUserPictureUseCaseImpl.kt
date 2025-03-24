package com.rdua.whiteboard.edit_profile.usecases

import com.rdua.whiteboard.firebase.state.UpdateUserPictureState
import com.rdua.whiteboard.repository.users.UsersRepository
import javax.inject.Inject

class DeleteUserPictureUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : DeleteUserPictureUseCase {
    override suspend fun invoke(id: String): UpdateUserPictureState {
        return usersRepository.deleteUserPicture(id)
    }
}