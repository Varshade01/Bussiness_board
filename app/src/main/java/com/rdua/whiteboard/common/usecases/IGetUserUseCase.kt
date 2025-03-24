package com.rdua.whiteboard.common.usecases

import com.rdua.whiteboard.firebase.state.GetUserState

interface IGetUserUseCase {

    suspend fun getCurrentUser(): GetUserState
    suspend fun getUser(id: String): GetUserState
    suspend fun getUserByEmail(email: String): GetUserState
}