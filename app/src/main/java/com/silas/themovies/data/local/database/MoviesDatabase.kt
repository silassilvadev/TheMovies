package com.silas.themovies.data.local.database

import android.content.Context
import androidx.room.Room

object MoviesDatabase {
    @Volatile
    private lateinit var database: AppDatabase

    fun instance(context: Context): AppDatabase {
        synchronized(this) {
            if (MoviesDatabase::database.isInitialized) return database
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "movies-db")
                .build()
            return database
        }
    }
}