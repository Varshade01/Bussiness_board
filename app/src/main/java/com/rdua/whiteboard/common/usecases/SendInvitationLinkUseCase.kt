package com.rdua.whiteboard.common.usecases

interface SendInvitationLinkUseCase {
    suspend fun sendInvitation(boardID: String)
}