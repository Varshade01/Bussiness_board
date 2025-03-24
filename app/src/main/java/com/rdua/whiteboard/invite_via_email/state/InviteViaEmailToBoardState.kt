package com.rdua.whiteboard.invite_via_email.state

sealed interface InviteViaEmailToBoardState {
    object InviteToBoardSuccess : InviteViaEmailToBoardState
    object InviteToBoardFailure : InviteViaEmailToBoardState
}
