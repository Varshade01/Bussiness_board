package com.rdua.whiteboard.edit_profile.usecases

import com.rdua.whiteboard.firebase.state.UpdateUserPictureState

interface UpdateUserPictureUseCase {
    suspend operator fun invoke(id: String, bytes: ByteArray): UpdateUserPictureState
}