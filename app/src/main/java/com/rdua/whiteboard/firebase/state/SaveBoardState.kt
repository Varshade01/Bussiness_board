package com.rdua.whiteboard.firebase.state

sealed interface SaveBoardState {
    object Success : SaveBoardState
    object Failure : SaveBoardState
}