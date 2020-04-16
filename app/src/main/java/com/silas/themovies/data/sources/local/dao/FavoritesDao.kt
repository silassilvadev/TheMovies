package com.silas.themovies.data.sources.local.dao

import androidx.room.*
import com.silas.themovies.model.entity.Movie
import io.reactivex.Maybe

/**
 * Here, queries to local data are mounted, and requested from the database
 *
 * @author Silas at 25/02/2020
 */
@Dao
interface FavoritesDao {

    @Query("SELECT * FROM Favorite")
    fun loadFavorites(): Maybe<List<Movie>>

    @Query("SELECT * FROM Favorite WHERE title LIKE '%' || :query || '%'")
    fun searchFavorites(query: String): Maybe<List<Movie>>

    @Query("SELECT * FROM Favorite WHERE id = :movieId")
    fun checkFavoriteId(movieId: Long): Maybe<Movie?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(vararg movie: Movie): Maybe<List<Long>>

    @Delete
    fun deleteFavorite(vararg movie: Movie): Maybe<Int>
}