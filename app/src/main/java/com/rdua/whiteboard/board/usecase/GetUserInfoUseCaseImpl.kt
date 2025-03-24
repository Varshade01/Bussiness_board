package com.rdua.whiteboard.board.usecase

import com.rdua.whiteboard.board.data.UserInfo
import com.rdua.whiteboard.common.usecases.GetUserNameUseCase
import com.rdua.whiteboard.firebase.state.GetUserStateSuccess
import com.rdua.whiteboard.repository.users.UsersRepository
import javax.inject.Inject

class GetUserInfoUseCaseImpl @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase,
    private val usersRepository: UsersRepository,
) : GetUserInfoUseCase {
    override suspend fun invoke(userId: String): UserInfo {
        val userName: String? = getUserNameUseCase(userId)

        val currentUserResult = usersRepository.getUser()
        val isThisUser = (currentUserResult as? GetUserStateSuccess)?.user?.id == userId

        val userResult = usersRepository.getUser(userId)
        val photoUrl: String? = (userResult as? GetUserStateSuccess)?.user?.photoUrl

        return UserInfo(name = userName, isThisUser = isThisUser, photoUrl = photoUrl)
    }
}