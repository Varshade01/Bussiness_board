package com.rdua.whiteboard.edit_profile.usecases

import com.rdua.whiteboard.firebase.state.UpdateUserPictureState

interface DeleteUserPictureUseCase {
    suspend operator fun invoke(id: String): UpdateUserPictureState
}