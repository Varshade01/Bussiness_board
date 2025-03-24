package com.rdua.whiteboard.registration.di

import com.rdua.whiteboard.registration.usecases.CreateUserUseCaseImpl
import com.rdua.whiteboard.registration.usecases.ICreateUserUseCase
import com.rdua.whiteboard.registration.usecases.IRegistrationUseCase
import com.rdua.whiteboard.registration.usecases.RegistrationUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RegistrationModule {

    @Binds
    abstract fun bindRegistrationUseCase(registrationUseCaseImpl: RegistrationUseCaseImpl): IRegistrationUseCase

    @Binds
    abstract fun bindCreateUserUseCase(createUserUseCaseImpl: CreateUserUseCaseImpl): ICreateUserUseCase

}