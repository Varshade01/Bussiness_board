package com.rdua.whiteboard.edit_profile.usecases

import com.rdua.whiteboard.firebase.state.UpdateUserPictureState
import com.rdua.whiteboard.repository.users.UsersRepository
import javax.inject.Inject

class UpdateUserPictureUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository
) : UpdateUserPictureUseCase {
    override suspend operator fun invoke(
        id: String,
        bytes: ByteArray
    ): UpdateUserPictureState {
        return usersRepository.updateUserPicture(id, bytes)
    }
}