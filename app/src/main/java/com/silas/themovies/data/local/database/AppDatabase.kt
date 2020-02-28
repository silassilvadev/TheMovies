package com.silas.themovies.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.silas.themovies.data.local.dao.MoviesDao
import com.silas.themovies.model.entity.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MoviesDao
}