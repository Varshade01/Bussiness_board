package com.rdua.whiteboard.home.usecase

import com.rdua.whiteboard.common.usecases.GetUserNameUseCase
import com.rdua.whiteboard.data.entities.boards.BoardEntity
import com.rdua.whiteboard.data.entities.boards.BoardMapper
import com.rdua.whiteboard.di.IoCoroutineScope
import com.rdua.whiteboard.repository.boards.BoardsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetBoardsUseCaseImpl @Inject constructor(
    @IoCoroutineScope val coroutineScope: CoroutineScope,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val boardsRepository: BoardsRepository,
    private val boardMapper: BoardMapper,
) : IGetBoardsUseCase {

    override fun getBoards(userId: String): StateFlow<List<BoardEntity>?> {
        val firebaseFlow = boardsRepository.getBoards(userId)

        val boardEntitiesFlow = firebaseFlow.map { firebaseBoards ->
            firebaseBoards?.map { fireBaseEntity ->
                boardMapper.toEntity(
                    entity = fireBaseEntity,
                    resolveStickyAuthor = { authorId ->
                        getUserNameUseCase(authorId)
                    }
                )
            }
        }.stateIn(coroutineScope, SharingStarted.Eagerly, null)

        return boardEntitiesFlow
    }
}
