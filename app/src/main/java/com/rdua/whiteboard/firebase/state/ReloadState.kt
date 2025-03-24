package com.rdua.whiteboard.firebase.state

sealed class ReloadState

object ReloadStateSuccess : ReloadState()

data class ReloadStateFailure(val error: ReloadStateError) : ReloadState()

enum class ReloadStateError {
    AuthInvalidUserException,
    UnknownFailure
}