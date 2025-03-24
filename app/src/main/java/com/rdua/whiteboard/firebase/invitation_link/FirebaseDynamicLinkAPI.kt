package com.rdua.whiteboard.firebase.invitation_link

interface FirebaseDynamicLinkAPI {
    fun generateDynamicLink(boardId: String)
}