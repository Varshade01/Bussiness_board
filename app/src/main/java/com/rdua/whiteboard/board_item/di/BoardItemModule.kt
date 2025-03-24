package com.rdua.whiteboard.board_item.di

import com.rdua.whiteboard.board_item.usecase.ClearBoardItemUseCaseImpl
import com.rdua.whiteboard.board_item.usecase.GetBoardItemInfoUseCaseImpl
import com.rdua.whiteboard.board_item.usecase.IClearBoardItemUseCase
import com.rdua.whiteboard.board_item.usecase.IGetBoardItemInfoUseCase
import com.rdua.whiteboard.board_item.usecase.ISubscribeToBoardUseCase
import com.rdua.whiteboard.board_item.usecase.SubscribeToBoardUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BoardItemModule {

    @Binds
    fun bindSubscribeToBoardUseCase(
        subscribeToBoardUseCase: SubscribeToBoardUseCaseImpl,
    ): ISubscribeToBoardUseCase

    @Binds
    fun bindClearBoardItemUseCase(clearBoardItemUseCase: ClearBoardItemUseCaseImpl): IClearBoardItemUseCase

    @Binds
    fun bindGetBoardItemInfoUseCase(getBoardItemInfoUseCase: GetBoardItemInfoUseCaseImpl): IGetBoardItemInfoUseCase
}
