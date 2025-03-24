package com.rdua.whiteboard.firebase.state

sealed interface UpdateBoardState {
    object Success : UpdateBoardState
    object Failure : UpdateBoardState
}