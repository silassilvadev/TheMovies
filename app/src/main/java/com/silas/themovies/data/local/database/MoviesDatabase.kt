package com.silas.themovies.data.local.database

import android.content.Context
import androidx.room.Room
import com.silas.themovies.data.local.database.AppDatabase.Companion.DATABASE_NAME

/**
 * Creates a database object for local data storage
 *
 * @property database Volatile instance of database
 *
 * @author Silas at 25/02/2020
 */
object  MoviesDatabase {
    @Volatile
    private lateinit var database: AppDatabase

    fun instance(context: Context): AppDatabase {
        synchronized(this) {
            if (MoviesDatabase::database.isInitialized) return database
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME)
                .build()
            return database
        }
    }
}