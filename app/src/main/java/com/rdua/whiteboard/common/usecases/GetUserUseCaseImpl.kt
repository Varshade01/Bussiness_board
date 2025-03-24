package com.rdua.whiteboard.common.usecases

import com.rdua.whiteboard.firebase.state.GetUserState
import com.rdua.whiteboard.repository.users.UsersRepository
import javax.inject.Inject

class GetUserUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository,
) : IGetUserUseCase {

    override suspend fun getCurrentUser(): GetUserState {
        return usersRepository.getUser()
    }

    override suspend fun getUser(id: String): GetUserState {
        return usersRepository.getUser(id)
    }

    override suspend fun getUserByEmail(email: String): GetUserState {
        return usersRepository.getUserByEmail(email)
    }
}