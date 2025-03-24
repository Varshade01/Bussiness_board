package com.rdua.whiteboard.common.usecases

import com.rdua.whiteboard.firebase.state.GetUserStateSuccess
import com.rdua.whiteboard.repository.users.UsersRepository
import javax.inject.Inject

class GetUserNameUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository,
) : GetUserNameUseCase {

    override suspend fun invoke(userId: String?): String? {
        return userId?.let {
            val result = usersRepository.getUser(userId)
            (result as? GetUserStateSuccess)?.user?.name
        }
    }
}