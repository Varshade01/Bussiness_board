package com.rdua.whiteboard.board.usecase

import com.rdua.whiteboard.board.data.UserInfo

interface GetUserInfoUseCase {
    suspend operator fun invoke(userId: String): UserInfo
}