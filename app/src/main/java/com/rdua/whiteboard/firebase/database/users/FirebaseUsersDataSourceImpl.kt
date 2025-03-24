package com.rdua.whiteboard.firebase.database.users

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.rdua.whiteboard.data.entities.users.FirebaseUserEntity
import com.rdua.whiteboard.data.entities.users.UserEntity
import com.rdua.whiteboard.data.entities.users.UserMapper
import com.rdua.whiteboard.firebase.state.GetUserState
import com.rdua.whiteboard.firebase.state.GetUserStateDatabaseException
import com.rdua.whiteboard.firebase.state.GetUserStateSuccess
import com.rdua.whiteboard.firebase.state.GetUserStateUnknownFailure
import com.rdua.whiteboard.firebase.state.RegistrationState
import com.rdua.whiteboard.firebase.state.RegistrationSuccess
import com.rdua.whiteboard.firebase.state.RegistrationUnknownFailure
import com.rdua.whiteboard.firebase.state.UpdateUserPictureState
import com.rdua.whiteboard.firebase.state.UpdateUserState
import com.rdua.whiteboard.firebase.state.UpdateUserStateDatabaseException
import com.rdua.whiteboard.firebase.state.UpdateUserStateNetworkFailure
import com.rdua.whiteboard.firebase.state.UpdateUserStateSuccess
import com.rdua.whiteboard.firebase.state.UpdateUserStateUnknownFailure
import com.rdua.whiteboard.firebase.state.isSuccess
import com.rdua.whiteboard.firebase.utils.DbConstant
import com.rdua.whiteboard.firebase.utils.DbConstant.FCMTOKENS
import com.rdua.whiteboard.firebase.utils.DbConstant.PHOTO_URL_FIELD
import com.rdua.whiteboard.firebase.utils.DbConstant.PROFILE_PATH
import com.rdua.whiteboard.firebase.utils.DbConstant.USERS_TABLE
import javax.inject.Inject
import kotlin.contracts.ExperimentalContracts
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseUsersDataSourceImpl @Inject constructor() : FirebaseUsersDataSource {

    private val database = Firebase.database.reference
    private val storage = FirebaseStorage.getInstance().reference
    private val usersTable = database.child(USERS_TABLE)

    override suspend fun createUserInFirebaseDB(
        id: String,
        name: String,
        email: String,
        temporaryUser: Boolean?
    ): RegistrationState {
        return suspendCoroutine { continuation ->
            val firebaseUser = FirebaseUserEntity(
                id = id,
                name = name,
                email = email,
                temporaryUser = temporaryUser
            )
            val callback = OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    continuation.resume(RegistrationSuccess(id))
                } else {
                    continuation.resume(RegistrationUnknownFailure)
                }
            }
            usersTable.child(id).setValue(firebaseUser).addOnCompleteListener(callback)
        }
    }

    override suspend fun getUserFromFirebase(id: String): GetUserState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<DataSnapshot> {
                if (it.isSuccessful) {
                    val firebaseUser = it.result.getValue(FirebaseUserEntity::class.java)
                    firebaseUser?.let {
                        continuation.resume(GetUserStateSuccess(UserMapper.toUserEntity(firebaseUser)))
                    } ?: continuation.resume(GetUserStateUnknownFailure)
                } else {
                    when (it.exception) {
                        is DatabaseException -> {
                            continuation.resume(GetUserStateDatabaseException)
                        }

                        else -> {
                            continuation.resume(GetUserStateUnknownFailure)
                        }
                    }
                }
            }
            usersTable.child(id).get().addOnCompleteListener(callback)
        }
    }

    override suspend fun getUserByEmailFromFirebase(email: String): GetUserState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<DataSnapshot> { task ->
                if (task.isSuccessful) {
                    // orderByChild query assumes that there could be multiple results. So you need to
                    // iterate through the list even if database is designed with unique emails.
                    val firebaseUser: FirebaseUserEntity?
                    val firstChildSnapshot = task.result.children.firstOrNull()
                    firebaseUser = firstChildSnapshot?.getValue(FirebaseUserEntity::class.java)
                    firebaseUser?.let {
                        continuation.resume(GetUserStateSuccess(UserMapper.toUserEntity(it)))
                    } ?: continuation.resume(GetUserStateUnknownFailure)
                } else {
                    when (task.exception) {
                        is DatabaseException -> {
                            continuation.resume(GetUserStateDatabaseException)
                        }

                        else -> {
                            continuation.resume(GetUserStateUnknownFailure)
                        }
                    }
                }
            }
            usersTable.orderByChild(DbConstant.PATH_EMAIL).equalTo(email).get()
                .addOnCompleteListener(callback)
        }
    }

    override suspend fun updateUserInFirebaseDB(user: UserEntity): UpdateUserState {
        return suspendCoroutine { continuation ->
            val firebaseUser = UserMapper.toFirebaseUserEntity(user)
            val callback = OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    continuation.resume(UpdateUserStateSuccess)
                } else {
                    when (task.exception) {
                        is FirebaseNetworkException -> {
                            continuation.resume(UpdateUserStateNetworkFailure)
                        }

                        is DatabaseException -> {
                            continuation.resume(UpdateUserStateDatabaseException)
                        }

                        else -> {
                            continuation.resume(UpdateUserStateUnknownFailure)
                        }
                    }
                }
            }
            usersTable.child(user.id).setValue(firebaseUser).addOnCompleteListener(callback)
        }
    }

    @OptIn(ExperimentalContracts::class)
    override suspend fun updateUserPicture(
        id: String,
        bytes: ByteArray
    ): UpdateUserPictureState {
        val storePictureInStorageResult = storeUserPictureInStorage(id, bytes)
        val getPictureUriFromStorageResult = getUserPictureUriFromStorage(id)

        return if (storePictureInStorageResult.isSuccess() && getPictureUriFromStorageResult.isSuccess()) {
            val url = getPictureUriFromStorageResult.url
            return updateUserPictureInDB(id, url)
        } else {
            UpdateUserPictureState.Failure
        }
    }

    @OptIn(ExperimentalContracts::class)
    override suspend fun deleteUserPicture(id: String): UpdateUserPictureState {
        val deleteFromStorageResult = deleteUserPictureFromStorage(id)
        val deleteFromRealtimeDBResult = deleteUserPictureFromRealtimeDB(id)

        return if (deleteFromStorageResult.isSuccess() && deleteFromRealtimeDBResult.isSuccess()) {
            UpdateUserPictureState.Success()
        } else {
            UpdateUserPictureState.Failure
        }
    }

    private suspend fun deleteUserPictureFromStorage(id: String): UpdateUserPictureState {
        return suspendCoroutine { continuation ->
            val profilePictureStorageRef = storage.child(USERS_TABLE).child(id).child(PROFILE_PATH)

            val callback = OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    continuation.resume(UpdateUserPictureState.Success())
                } else {
                    continuation.resume(UpdateUserPictureState.Failure)
                }
            }
            profilePictureStorageRef.delete().addOnCompleteListener(callback)
        }
    }

    private suspend fun deleteUserPictureFromRealtimeDB(id: String): UpdateUserPictureState {
        return suspendCoroutine { continuation ->
            val profilePictureRealtimeDBRef = usersTable.child(id).child(PHOTO_URL_FIELD)

            val callback = OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    continuation.resume(UpdateUserPictureState.Success())
                } else {
                    continuation.resume(UpdateUserPictureState.Failure)
                }
            }
            profilePictureRealtimeDBRef.removeValue().addOnCompleteListener(callback)
        }
    }

    private suspend fun storeUserPictureInStorage(
        id: String,
        bytes: ByteArray
    ): UpdateUserPictureState {
        return suspendCoroutine { continuation ->
            val profilePictureStorageRef = storage.child(USERS_TABLE).child(id).child(PROFILE_PATH)

            val callback = OnCompleteListener<UploadTask.TaskSnapshot> { task ->
                if (task.isSuccessful) {
                    continuation.resume(UpdateUserPictureState.Success())
                } else {
                    continuation.resume(UpdateUserPictureState.Failure)
                }
            }
            profilePictureStorageRef.putBytes(bytes).addOnCompleteListener(callback)
        }
    }

    private suspend fun getUserPictureUriFromStorage(id: String): UpdateUserPictureState {
        return suspendCoroutine { continuation ->
            val profilePictureStorageRef = storage.child(USERS_TABLE).child(id).child(PROFILE_PATH)

            val callback = OnCompleteListener<Uri> { task ->
                if (task.isSuccessful) {
                    continuation.resume(UpdateUserPictureState.Success(url = task.result.toString()))
                } else {
                    continuation.resume(UpdateUserPictureState.Failure)
                }
            }
            profilePictureStorageRef.downloadUrl.addOnCompleteListener(callback)
        }
    }

    private suspend fun updateUserPictureInDB(id: String, url: String): UpdateUserPictureState {
        return suspendCoroutine { continuation ->
            val callback = OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    continuation.resume(UpdateUserPictureState.Success(url = url))
                } else {
                    continuation.resume(UpdateUserPictureState.Failure)
                }
            }
            usersTable.child(id).child(PHOTO_URL_FIELD).setValue(url)
                .addOnCompleteListener(callback)
        }
    }

    override suspend fun deleteUserData(
        id: String,
    ): UpdateUserState {
        val deletedUser = FirebaseUserEntity(id = id, isDeleted = true)
        return suspendCoroutine { continuation ->

            val callback = OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    continuation.resume(UpdateUserStateSuccess)
                } else {
                    when (task.exception) {
                        is FirebaseNetworkException -> {
                            continuation.resume(UpdateUserStateNetworkFailure)
                        }

                        is DatabaseException -> {
                            continuation.resume(UpdateUserStateDatabaseException)
                        }

                        else -> {
                            continuation.resume(UpdateUserStateUnknownFailure)
                        }
                    }
                }
            }
            usersTable.child(id).setValue(deletedUser).addOnCompleteListener(callback)
        }
    }

    @OptIn(ExperimentalContracts::class)
    override suspend fun checkPhotoExists(id: String): Boolean {
        val pictureUriResult = getUserPictureUriFromStorage(id)
        return pictureUriResult.isSuccess()
    }

    override fun updateFcmToken(id: String, token: String) {
        usersTable.child(id).child(FCMTOKENS).child(token).setValue(token)
    }

    override fun removeFcmToken(id: String, token: String) {
        usersTable.child(id).child(FCMTOKENS).child(token).removeValue()
    }
}