package com.rdua.whiteboard.invite_via_email.usecases

import com.rdua.whiteboard.invite_via_email.state.AddUserToBoardState

interface IAddUserToBoardUseCase {
    suspend operator fun invoke(boardId: String, userId: String): AddUserToBoardState
}