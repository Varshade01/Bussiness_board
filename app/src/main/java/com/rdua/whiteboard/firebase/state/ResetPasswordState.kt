package com.rdua.whiteboard.firebase.state

sealed class ResetPasswordState

object ResetPasswordStateSuccess : ResetPasswordState()

object NetworkFailure : ResetPasswordState()

object UserIsNotExists : ResetPasswordState()

object UnknownFailure : ResetPasswordState()
