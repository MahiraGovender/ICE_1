package com.example.betwise.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface userDAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: userEntity)

    @Query("SELECT * FROM Users WHERE userName = :userName LIMIT 1")
    suspend fun getByUserName(userName: String): userEntity?

    @Query("SELECT * FROM Users WHERE userName = :userName AND password = :password LIMIT 1")
    suspend fun loginUser(userName: String, password: String): userEntity?
}