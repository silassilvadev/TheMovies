package com.silas.themovies.data.local.dao

import androidx.room.*
import com.silas.themovies.model.dto.response.Movie
import kotlinx.coroutines.Deferred

/**
 * Here, queries to local data are mounted, and requested from the database
 *
 * @author Silas at 25/02/2020
 */
@Dao
interface MoviesDao {

    @Query("SELECT * FROM Favorite")
    suspend fun loadFavorites(): List<Movie>

    @Query("SELECT * FROM Favorite WHERE title LIKE '%' || :query || '%'")
    suspend fun searchFavorites(query: String): List<Movie>

    @Query("SELECT * FROM Favorite WHERE id = :movieId")
    suspend fun loadFavoriteId(movieId: Long): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(vararg movie: Movie): List<Long>

    @Delete
    suspend fun deleteFavorite(vararg movie: Movie): Int
}