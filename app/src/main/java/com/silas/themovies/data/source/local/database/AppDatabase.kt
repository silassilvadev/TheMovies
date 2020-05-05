package com.silas.themovies.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.silas.themovies.data.source.local.dao.FavoritesDao
import com.silas.themovies.model.entity.Movie

/**
 * Provides a Dao instance for data persistence
 *
 * @author Silas at 25/02/2020
 */
@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
}