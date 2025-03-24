package com.rdua.whiteboard.data.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rdua.whiteboard.data.entities.users.UserEntity

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id=:id")
    suspend fun getUser(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE email=:email")
    suspend fun getUserByEmail(email: String): UserEntity

    @Update(entity = UserEntity::class)
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)
}