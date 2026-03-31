package com.example.betwise.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Users",
    indices = [Index(value = ["userName"], unique = true)]
)
data class userEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userName: String,
    val email: String,
    val password: String,
    val fullName: String // Added fullName field
)