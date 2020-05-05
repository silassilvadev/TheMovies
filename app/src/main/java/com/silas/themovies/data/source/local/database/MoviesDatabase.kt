package com.silas.themovies.data.source.local.database

import android.content.Context
import androidx.room.Room

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
    private const val DATABASE_NAME = "movies-db"

    fun instance(context: Context): AppDatabase {
        synchronized(this) {
            if (MoviesDatabase::database.isInitialized) return database
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
            return database
        }
    }
}