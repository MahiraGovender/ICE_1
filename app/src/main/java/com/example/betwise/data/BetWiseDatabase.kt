package com.example.betwise.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [userEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BetWiseDatabase : RoomDatabase() {
    abstract fun userDao(): userDAO

    companion object {
        @Volatile
        private var INSTANCE: BetWiseDatabase? = null

        fun getDatabase(context: Context): BetWiseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BetWiseDatabase::class.java,
                    "betwise_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}