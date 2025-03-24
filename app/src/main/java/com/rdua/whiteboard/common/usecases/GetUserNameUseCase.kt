package com.rdua.whiteboard.common.usecases

interface GetUserNameUseCase {
    suspend operator fun invoke(userId: String?): String?
}