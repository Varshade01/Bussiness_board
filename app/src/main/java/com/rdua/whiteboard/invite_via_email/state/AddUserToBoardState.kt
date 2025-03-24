package com.rdua.whiteboard.invite_via_email.state

sealed interface AddUserToBoardState {
    object Success : AddUserToBoardState
    object Failure : AddUserToBoardState
}