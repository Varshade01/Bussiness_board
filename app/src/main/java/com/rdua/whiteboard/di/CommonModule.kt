package com.rdua.whiteboard.di

import com.rdua.whiteboard.common.provider.ResourceProvider
import com.rdua.whiteboard.common.provider.ResourceProviderImpl
import com.rdua.whiteboard.common.usecases.GetUserNameUseCase
import com.rdua.whiteboard.common.usecases.GetUserNameUseCaseImpl
import com.rdua.whiteboard.common.usecases.GetUserUseCaseImpl
import com.rdua.whiteboard.common.usecases.IGetUserUseCase
import com.rdua.whiteboard.common.usecases.SendInvitationLinkUseCase
import com.rdua.whiteboard.common.usecases.SendInvitationLinkUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Binds
    abstract fun bindGetUserUseCase(getUserUseCaseImpl: GetUserUseCaseImpl): IGetUserUseCase

    @Binds
    abstract fun bindGetUserNameUseCase(getUserNameUseCaseImpl: GetUserNameUseCaseImpl): GetUserNameUseCase

    @Binds
    abstract fun bindSendInvitationLinkUseCase(sendInvitationLinkUseCase: SendInvitationLinkUseCaseImpl): SendInvitationLinkUseCase

    @Binds
    abstract fun bindResourceProvider(resourceProviderImpl: ResourceProviderImpl): ResourceProvider
}