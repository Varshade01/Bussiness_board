package com.rdua.whiteboard.board.di

import com.rdua.whiteboard.board.usecase.CheckBlockTokenValidityUseCase
import com.rdua.whiteboard.board.usecase.CheckBlockTokenValidityUseCaseImpl
import com.rdua.whiteboard.board.usecase.DeleteBoardItemUseCase
import com.rdua.whiteboard.board.usecase.DeleteBoardItemUseCaseImpl
import com.rdua.whiteboard.board.usecase.GetUserInfoUseCase
import com.rdua.whiteboard.board.usecase.GetUserInfoUseCaseImpl
import com.rdua.whiteboard.board.usecase.SaveBoardItemUseCase
import com.rdua.whiteboard.board.usecase.SaveBoardItemUseCaseImpl
import com.rdua.whiteboard.board.usecase.SaveBoardStateUseCase
import com.rdua.whiteboard.board.usecase.SaveBoardStateUseCaseImpl
import com.rdua.whiteboard.common.usecases.GetDeviceIdUseCase
import com.rdua.whiteboard.common.usecases.GetDeviceIdUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BoardModule {
    @Binds
    fun bindSaveBoardItemUseCase(
        saveBoardItemUseCaseImpl: SaveBoardItemUseCaseImpl,
    ): SaveBoardItemUseCase

    @Binds
    fun bindSaveBoardStateUseCase(
        saveBoardStateUseCaseImpl: SaveBoardStateUseCaseImpl,
    ): SaveBoardStateUseCase

    @Binds
    fun bindDeleteBoardItemUseCase(
        deleteBoardItemUseCase: DeleteBoardItemUseCaseImpl,
    ): DeleteBoardItemUseCase

    @Binds
    fun bindGetDeviceIdUseCase(
        getDeviceIdUseCaseImpl: GetDeviceIdUseCaseImpl,
    ): GetDeviceIdUseCase

    @Binds
    fun bindGetUserInfoUseCase(
        getUserInfoUseCaseImpl: GetUserInfoUseCaseImpl,
    ): GetUserInfoUseCase

    @Binds
    fun bindCheckBlockTokenValidityUseCase(
        checkBlockTokenValidityUseCaseImpl: CheckBlockTokenValidityUseCaseImpl,
    ): CheckBlockTokenValidityUseCase
}