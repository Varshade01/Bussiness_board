package com.rdua.whiteboard.settings.usecase

import com.rdua.whiteboard.edit_profile.usecases.DeleteUserPictureUseCase
import com.rdua.whiteboard.firebase.state.DeleteAccountState
import com.rdua.whiteboard.firebase.auth.FirebaseAuthApi
import com.rdua.whiteboard.firebase.state.ReauthState
import com.rdua.whiteboard.firebase.state.UpdateUserPictureState
import com.rdua.whiteboard.firebase.state.UpdateUserState
import com.rdua.whiteboard.firebase.state.UpdateUserStateSuccess
import com.rdua.whiteboard.firebase.database.users.FirebaseUsersDataSource
import com.rdua.whiteboard.repository.users.UsersRepository
import javax.inject.Inject

class DeleteAccountUseCaseImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuthApi,
    private val firebaseUsersDataSource: FirebaseUsersDataSource,
    private val usersRepository: UsersRepository,
    private val deleteUserPictureUseCase: DeleteUserPictureUseCase,
) : DeleteAccountUseCase {

    override suspend fun deleteAccount(
        password: String
    ): DeleteAccountState {
        return when (firebaseAuth.reauthenticate(password)) {
            is ReauthState.ReauthStateSuccess -> {
                val userId = firebaseAuth.currentUserId()
                if (userId != null) {
                    val deleteResult = if (firebaseUsersDataSource.checkPhotoExists(userId)) {
                        when (deletePicture(userId)) {
                            is UpdateUserPictureState.Success -> DeleteAccountState.DeleteAccountSuccess
                            else -> DeleteAccountState.UnknownFailure
                        }
                    } else {
                        DeleteAccountState.DeleteAccountSuccess
                    }

                    return if (deleteResult == DeleteAccountState.DeleteAccountSuccess) {
                        deleteAccountIfReauthIsSuccess(userId)
                    } else {
                        deleteResult
                    }
                } else {
                    DeleteAccountState.UnknownFailure
                }
            }

            is ReauthState.NetworkFailure -> DeleteAccountState.NetworkFailure
            is ReauthState.InvalidCredentials -> DeleteAccountState.InvalidCredentials
            is ReauthState.UserIsNotExist -> DeleteAccountState.UserIsNotExist
            else -> DeleteAccountState.UnknownFailure
        }
    }

    private suspend fun deleteAccountIfReauthIsSuccess(userId: String): DeleteAccountState {
        val deleteUserFromRealtimeDBResult = deleteUserFromRealtimeDB(userId)
        return if (deleteUserFromRealtimeDBResult is UpdateUserStateSuccess) {
            usersRepository.deleteUserFromDB(userId)
            usersRepository.deleteUserFromCache(userId)
            firebaseAuth.deleteAccount()
        } else {
            DeleteAccountState.UnknownFailure
        }
    }

    private suspend fun deletePicture(userId: String): UpdateUserPictureState {
        return deleteUserPictureUseCase.invoke(userId)
    }

    private suspend fun deleteUserFromRealtimeDB(
        userId: String
    ): UpdateUserState {
        return firebaseUsersDataSource.deleteUserData(userId)
    }
}
