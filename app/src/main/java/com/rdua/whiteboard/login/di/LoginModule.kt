package com.rdua.whiteboard.login.di

import com.rdua.whiteboard.login.usecases.LoginUseCase
import com.rdua.whiteboard.login.usecases.LoginUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginModule {
    @Binds
    abstract fun bindLoginUseCase(loginUseCaseImpl: LoginUseCaseImpl): LoginUseCase
}