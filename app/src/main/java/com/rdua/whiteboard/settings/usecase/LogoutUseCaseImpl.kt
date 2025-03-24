package com.rdua.whiteboard.settings.usecase

import com.rdua.whiteboard.common.usecases.RemoveFcmTokenUseCase
import com.rdua.whiteboard.repository.users.UsersRepository
import com.rdua.whiteboard.settings.state.LogoutState
import javax.inject.Inject

class LogoutUseCaseImpl @Inject constructor(
    private val usersRepository: UsersRepository,
    private val removeFcmTokenUseCase: RemoveFcmTokenUseCase
) : ILogoutUseCase {
    override suspend fun logoutUser(): LogoutState {
        val logoutState = usersRepository.logoutUser()
        if (logoutState is LogoutState.LogoutSuccess) { removeFcmTokenUseCase.removeFcmToken() }
        return logoutState
    }
}
