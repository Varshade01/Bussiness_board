package com.rdua.whiteboard.edit_profile.di

import com.rdua.whiteboard.edit_profile.usecases.ChangeAuthEmailUseCase
import com.rdua.whiteboard.edit_profile.usecases.ChangeAuthEmailUseCaseImpl
import com.rdua.whiteboard.edit_profile.usecases.DeleteUserPictureUseCase
import com.rdua.whiteboard.edit_profile.usecases.DeleteUserPictureUseCaseImpl
import com.rdua.whiteboard.edit_profile.usecases.IUpdateUserUseCase
import com.rdua.whiteboard.edit_profile.usecases.UpdateUserPictureUseCase
import com.rdua.whiteboard.edit_profile.usecases.UpdateUserPictureUseCaseImpl
import com.rdua.whiteboard.edit_profile.usecases.UpdateUserUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EditProfileModule {
    @Binds
    abstract fun updateUserUseCase(updateUserUseCaseImpl: UpdateUserUseCaseImpl): IUpdateUserUseCase

    @Binds
    abstract fun bindChangeUserPictureUseCase(
        updateUserPictureUseCase: UpdateUserPictureUseCaseImpl
    ): UpdateUserPictureUseCase

    @Binds
    abstract fun bindDeleteProfilePictureUseCase(
        deleteUserPictureUseCase: DeleteUserPictureUseCaseImpl
    ): DeleteUserPictureUseCase

    @Binds
    abstract fun bindChangeAuthEmailUseCase(
        changeAuthEmailUseCase: ChangeAuthEmailUseCaseImpl
    ): ChangeAuthEmailUseCase
}