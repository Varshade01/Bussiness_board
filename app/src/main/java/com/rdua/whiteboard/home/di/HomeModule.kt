package com.rdua.whiteboard.home.di

import com.rdua.whiteboard.home.usecase.CreateBoardUseCaseImpl
import com.rdua.whiteboard.home.usecase.GetBoardsUseCaseImpl
import com.rdua.whiteboard.home.usecase.ICreateBoardUseCase
import com.rdua.whiteboard.home.usecase.IGetBoardsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    abstract fun bindCreateBoardUseCase(createBoardUseCase: CreateBoardUseCaseImpl): ICreateBoardUseCase

    @Binds
    abstract fun bindGetBoardsUseCase(getBoardsUseCase: GetBoardsUseCaseImpl): IGetBoardsUseCase
}
