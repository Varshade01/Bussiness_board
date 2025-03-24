package com.rdua.whiteboard.firebase.database.boards

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.rdua.whiteboard.board_options_dialog.state.DeleteBoardState
import com.rdua.whiteboard.data.entities.boards.FirebaseBoardEntity
import com.rdua.whiteboard.data.entities.boards.access.AccessInfo
import com.rdua.whiteboard.data.entities.boards.provider.UpdateBoardStateData
import com.rdua.whiteboard.firebase.state.SaveBoardState
import com.rdua.whiteboard.firebase.state.UpdateBoardState
import com.rdua.whiteboard.firebase.utils.DbConstant
import com.rdua.whiteboard.home.state.BoardState
import com.rdua.whiteboard.home.state.BoardState.BoardSuccess
import com.rdua.whiteboard.home.state.BoardState.CreateBoardFailure
import com.rdua.whiteboard.home.state.BoardState.KeyBoardFailure
import com.rdua.whiteboard.invite_via_email.state.AddUserToBoardState
import com.rdua.whiteboard.rename_board.state.RenameBoardState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseBoardsDataSourceImpl @Inject constructor() : IFirebaseBoardsDataSource {

    private val database = Firebase.database.reference
    private val boardsTable = database.child(DbConstant.BOARDS)

    private val cachedBoardsValueListener = mutableSetOf<ValueEventListener>()
    private val _boardsFlow = MutableStateFlow<List<FirebaseBoardEntity>?>(null)

    private val boardsValueEventListener: ValueEventListener =
        object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            // Might not be called properly, if DB updates many times in a small amount of time (every second).
            override fun onDataChange(snapshot: DataSnapshot) {
                val boards = mutableListOf<FirebaseBoardEntity>()

                snapshot.children.forEach { dataSnapshot ->
                    dataSnapshot
                        .getValue<FirebaseBoardEntity>()
                        ?.let { databaseBoard -> boards.add(databaseBoard) }
                }
                _boardsFlow.tryEmit(boards)
            }
        }

    override fun getBoards(userId: String): StateFlow<List<FirebaseBoardEntity>?> {
        // Getting boards by matching "userId" from the list of "users" with access to this board.
        val queryUserId =
            boardsTable.orderByChild("${DbConstant.PATH_USERS}/$userId/${DbConstant.PATH_USER_ID}")
        val query = queryUserId.equalTo(userId)

        query.addValueEventListener(boardsValueEventListener)
            .also { cachedBoardsValueListener.add(it) }

        return _boardsFlow
    }

    override fun clearBoardsCache() {
        cachedBoardsValueListener.forEach { boardsTable.removeEventListener(it) }
        cachedBoardsValueListener.clear()
        _boardsFlow.tryEmit(null)
    }

    override suspend fun createBoard(boardEntity: FirebaseBoardEntity): BoardState =
        suspendCoroutine { continuation ->
            val boardId = boardsTable.push().key

            if (boardId == null) {
                continuation.resume(KeyBoardFailure)
            } else {
                val callback = OnCompleteListener<Void> { task ->
                    if (task.isSuccessful)
                        continuation.resume(BoardSuccess(boardId))
                    else
                        continuation.resume(CreateBoardFailure)
                }
                boardsTable
                    .child(boardId)
                    .setValue(boardEntity.copy(id = boardId))
                    .addOnCompleteListener(callback)
            }
        }

    override suspend fun renameBoard(boardEntity: FirebaseBoardEntity, newName: String): RenameBoardState {
        return suspendCoroutine { continuation ->

            val callback = OnCompleteListener<Void> { task ->
                if (task.isSuccessful)
                    continuation.resume(RenameBoardState.RenameBoardSuccess)
                else
                    continuation.resume(RenameBoardState.RenameBoardFailure)
            }

            boardsTable.child(boardEntity.id!!)
                .setValue(boardEntity.copy(title = newName))
                .addOnCompleteListener(callback)
        }
    }

    override suspend fun addUserToBoard(
        boardId: String,
        accessInfo: AccessInfo
    ): AddUserToBoardState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    continuation.resume(AddUserToBoardState.Success)
                } else {
                    continuation.resume(AddUserToBoardState.Failure)
                }
            }

            boardsTable.child(boardId)
                .child(DbConstant.PATH_USERS)
                .updateChildren(mapOf(accessInfo.userId to accessInfo))
                .addOnCompleteListener(callback)
        }
    }

    override suspend fun updateBoardState(
        data: UpdateBoardStateData,
    ): UpdateBoardState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    continuation.resume(UpdateBoardState.Success)
                } else {
                    continuation.resume(UpdateBoardState.Failure)
                }
            }
            boardsTable.child(data.path)
                .updateChildren(data.data)
                .addOnCompleteListener(callback)
        }
    }

    override suspend fun saveBoardState(
        data: UpdateBoardStateData,
    ): SaveBoardState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    continuation.resume(SaveBoardState.Success)
                } else {
                    continuation.resume(SaveBoardState.Failure)
                }
            }
            boardsTable.child(data.path)
                .setValue(data.data)
                .addOnCompleteListener(callback)
        }
    }

    override suspend fun deleteBoard(id: String): DeleteBoardState =
        suspendCoroutine { continuation ->

            val completeListener = OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    continuation.resume(DeleteBoardState.DeleteSuccess)
                } else {
                    continuation.resume(DeleteBoardState.DeleteFailure)
                }
            }
            boardsTable.child(id).removeValue().addOnCompleteListener(completeListener)
        }
}
