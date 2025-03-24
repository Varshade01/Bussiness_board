package com.rdua.whiteboard.common.usecases

import com.rdua.whiteboard.firebase.invitation_link.FirebaseDynamicLinkAPI
import javax.inject.Inject

class SendInvitationLinkUseCaseImpl @Inject constructor(
    private val firebaseDynamicLinkAPI: FirebaseDynamicLinkAPI
) : SendInvitationLinkUseCase {
    override suspend fun sendInvitation(boardID: String) {
        firebaseDynamicLinkAPI.generateDynamicLink(boardID)
    }
}