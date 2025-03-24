package com.rdua.whiteboard.invite_via_email.di

import com.rdua.whiteboard.invite_via_email.usecases.AddUserToBoardUseCaseImpl
import com.rdua.whiteboard.invite_via_email.usecases.IAddUserToBoardUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InviteViaEmailModule {
    @Binds
    fun bindAddUserToBoardUseCase(addUserToBoardUseCase: AddUserToBoardUseCaseImpl): IAddUserToBoardUseCase
}