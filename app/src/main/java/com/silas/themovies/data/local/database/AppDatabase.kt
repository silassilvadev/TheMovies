package com.silas.themovies.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.silas.themovies.data.local.dao.MoviesDao
import com.silas.themovies.model.dto.response.Movie

/**
 * Provides a Dao instance for data persistence
 *
 * @author Silas at 25/02/2020
 */
@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MoviesDao
}