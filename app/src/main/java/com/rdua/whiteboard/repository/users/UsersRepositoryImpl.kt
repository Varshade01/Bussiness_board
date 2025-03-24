package com.rdua.whiteboard.repository.users

import com.rdua.whiteboard.data.entities.users.UserEntity
import com.rdua.whiteboard.data.room_database.UsersDao
import com.rdua.whiteboard.firebase.auth.FirebaseAuth
import com.rdua.whiteboard.firebase.connection.FirebaseConnection
import com.rdua.whiteboard.firebase.database.users.FirebaseUsersDataSource
import com.rdua.whiteboard.firebase.state.GetUserState
import com.rdua.whiteboard.firebase.state.GetUserStateRoomNotFound
import com.rdua.whiteboard.firebase.state.GetUserStateSuccess
import com.rdua.whiteboard.firebase.state.RegistrationState
import com.rdua.whiteboard.firebase.state.RegistrationSuccess
import com.rdua.whiteboard.firebase.state.UpdateUserPictureState
import com.rdua.whiteboard.firebase.state.UpdateUserState
import com.rdua.whiteboard.firebase.state.UpdateUserStateSuccess
import com.rdua.whiteboard.registration.model.RegistrationFields
import com.rdua.whiteboard.settings.state.LogoutState
import java.util.Collections
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val firebaseUsersDataSource: FirebaseUsersDataSource,
    private val usersDao: UsersDao,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseConnection: FirebaseConnection,
    private val firebaseUsersDatabase: FirebaseUsersDataSource,
) : UsersRepository {

    companion object {
        private val cachedUsers: MutableList<UserEntity> =
            Collections.synchronizedList(mutableListOf())
    }

    private suspend fun addUserInRoom(userEntity: UserEntity) = usersDao.addUser(userEntity)

    private fun addUserInCache(newUser: UserEntity) {

        val cachedUser = cachedUsers.find { it.id == newUser.id }
        if (cachedUser == null) {
            cachedUsers.add(newUser)
        } else {
            cachedUsers.remove(cachedUser)
            cachedUsers.add(newUser)
        }
    }

    private suspend fun updateUserInRoom(user: UserEntity) = usersDao.updateUser(user)

    private fun updateUserInCache(user: UserEntity) {
        val index = cachedUsers.indexOfFirst { it.id == user.id }
        if (index == -1) {
            //User doesn't exist in cache, add it
            cachedUsers.add(user)
        } else {
            //User exists in cache, update it
            cachedUsers[index] = user
        }
    }

    override suspend fun createUser(
        state: RegistrationState,
        registrationFields: RegistrationFields,
    ): RegistrationState {
        return when (state) {
            is RegistrationSuccess -> {
                firebaseUsersDataSource.createUserInFirebaseDB(
                    id = state.userId,
                    name = registrationFields.name,
                    email = registrationFields.email,
                    temporaryUser = registrationFields.temporaryUser
                )
                val userEntity = UserEntity(
                    id = state.userId,
                    name = registrationFields.name,
                    email = registrationFields.email,
                )
                addUserInRoom(userEntity)
                addUserInCache(userEntity)
                state
            }

            else -> state
        }
    }


    override suspend fun getUser(): GetUserState {
        val currentUserId = firebaseAuth.currentUser()?.uid
        return getUser(currentUserId!!)
    }

    override suspend fun getUser(id: String): GetUserState {
        val cachedUser = cachedUsers.find { it.id == id }

        return when {
            cachedUser != null -> {
                GetUserStateSuccess(cachedUser)
            }

            !firebaseConnection.getConnection() -> {
                val userInRoom = usersDao.getUser(id)
                if (userInRoom != null) {
                    GetUserStateSuccess(userInRoom)
                } else {
                    GetUserStateRoomNotFound
                }
            }

            else -> {
                val getUserStateFromFirebase =
                    firebaseUsersDataSource.getUserFromFirebase(id)
                return if (getUserStateFromFirebase is GetUserStateSuccess) {
                    addUserInCache(getUserStateFromFirebase.user)
                    addUserInRoom(getUserStateFromFirebase.user)
                    GetUserStateSuccess(getUserStateFromFirebase.user)
                } else {
                    getUserStateFromFirebase
                }
            }
        }
    }

    override suspend fun getUserByEmail(email: String): GetUserState {
        val cachedUser = cachedUsers.find { it.email == email }

        return when {
            cachedUser != null -> {
                GetUserStateSuccess(cachedUser)
            }

            !firebaseConnection.getConnection() -> {
                val userInRoom = usersDao.getUserByEmail(email)
                GetUserStateSuccess(userInRoom)
            }

            else -> {
                val getUserStateFromFirebase =
                    firebaseUsersDataSource.getUserByEmailFromFirebase(email)
                return if (getUserStateFromFirebase is GetUserStateSuccess) {
                    addUserInCache(getUserStateFromFirebase.user)
                    addUserInRoom(getUserStateFromFirebase.user)
                    GetUserStateSuccess(getUserStateFromFirebase.user)
                } else {
                    getUserStateFromFirebase
                }
            }
        }
    }

    override suspend fun logoutUser(): LogoutState = firebaseAuth.logoutUser()

    override suspend fun updateUserPicture(
        id: String,
        bytes: ByteArray,
    ): UpdateUserPictureState {
        val result = firebaseUsersDataSource.updateUserPicture(id, bytes)
        if (result is UpdateUserPictureState.Success) {
            cachedUsers.find { it.id == id }?.let { cachedUser ->
                val updatedUser = cachedUser.copy(photoUrl = result.url)
                updateUserInCache(updatedUser)
                updateUserInRoom(updatedUser)
            }
        }
        return result
    }

    override suspend fun updateUser(user: UserEntity): UpdateUserState {
        val firebaseState = firebaseUsersDataSource.updateUserInFirebaseDB(user)
        if (firebaseState is UpdateUserStateSuccess) {
            updateUserInRoom(user)
            updateUserInCache(user)
        }
        return firebaseState
    }

    override suspend fun deleteUserPicture(id: String): UpdateUserPictureState {
        val result = firebaseUsersDataSource.deleteUserPicture(id)
        if (result is UpdateUserPictureState.Success) {
            cachedUsers.find { it.id == id }?.let { cachedUser ->
                val updatedUser = cachedUser.copy(photoUrl = null)
                updateUserInCache(updatedUser)
                updateUserInRoom(updatedUser)
            }
        }
        return result
    }

    override suspend fun deleteUserFromDB(id: String) {
        usersDao.getUser(id)?.let { usersDao.deleteUser(it) }
    }

    override suspend fun deleteUserFromCache(id: String) {
        val cachedUser = cachedUsers.find { it.id == id }
        cachedUser?.let {
            cachedUsers.remove(cachedUser)
        }
    }

    override fun updateFcmToken(id: String, token: String) {
        firebaseUsersDatabase.updateFcmToken(id, token)
    }

    override fun removeFcmToken(id: String, token: String) {
        firebaseUsersDatabase.removeFcmToken(id, token)
    }
}
