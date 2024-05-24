package com.app.movies.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.movies.data.local.db.dao.VideoDao
import com.app.movies.data.model.VideoEntity

@Database(
    entities = [VideoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun videosDao(): VideoDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null
        private const val DATABASE_NAME = "moviesDb"

        fun getInstance(context: Context): LocalDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(appContext: Context): LocalDatabase {
            return Room.databaseBuilder(appContext, LocalDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
