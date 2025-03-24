package com.rdua.whiteboard.data.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rdua.whiteboard.data.entities.users.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}