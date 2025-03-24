package com.rdua.whiteboard.change_password.di

import com.rdua.whiteboard.change_password.usecase.ChangePasswordUseCase
import com.rdua.whiteboard.change_password.usecase.ChangePasswordUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ChangePasswordModule {

    @Binds
    abstract fun bindChangePasswordUseCase(
        changePasswordUseCaseImpl: ChangePasswordUseCaseImpl
    ): ChangePasswordUseCase
}