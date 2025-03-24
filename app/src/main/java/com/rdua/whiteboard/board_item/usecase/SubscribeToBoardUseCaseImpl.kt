package com.rdua.whiteboard.board_item.usecase

import com.rdua.whiteboard.common.usecases.GetUserNameUseCase
import com.rdua.whiteboard.data.entities.boards.BoardEntityState
import com.rdua.whiteboard.data.entities.boards.BoardMapper
import com.rdua.whiteboard.di.IoCoroutineScope
import com.rdua.whiteboard.repository.boards.BoardsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SubscribeToBoardUseCaseImpl @Inject constructor(
    @IoCoroutineScope val coroutineScope: CoroutineScope,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val boardsRepository: BoardsRepository,
    private val boardMapper: BoardMapper,
) : ISubscribeToBoardUseCase {

    override fun subscribeToBoard(id: String?): StateFlow<BoardEntityState?> {
        val firebaseEntityFlow = boardsRepository.subscribeToBoard(id = id)

        return firebaseEntityFlow.map {
            it?.let { fireBaseEntity ->
                boardMapper.toEntity(
                    entity = fireBaseEntity,
                    resolveStickyAuthor = { authorId ->
                        getUserNameUseCase(authorId)
                    }
                )
            }
        }.stateIn(coroutineScope, SharingStarted.Eagerly, BoardEntityState.Loading)
    }
}