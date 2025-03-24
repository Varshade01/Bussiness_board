package com.rdua.whiteboard.reset_password.di

import com.rdua.whiteboard.reset_password.usecases.ResetPasswordUseCase
import com.rdua.whiteboard.reset_password.usecases.ResetPasswordUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class ResetPasswordModule {

    @Binds
    abstract fun bindResetPasswordUseCase(resetPasswordUseCase: ResetPasswordUseCaseImpl): ResetPasswordUseCase
}