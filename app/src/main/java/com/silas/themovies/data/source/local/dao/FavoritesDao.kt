package com.silas.themovies.data.source.local.dao

import androidx.room.*
import com.silas.themovies.model.entity.Movie
import io.reactivex.Single

/**
 * Here, queries to local data are mounted, and requested from the database
 *
 * @author Silas at 25/02/2020
 */
@Dao
interface FavoritesDao {

    @Query("SELECT * FROM Favorite")
    fun loadFavorites(): Single<List<Movie>>

    @Query("SELECT * FROM Favorite WHERE title LIKE '%' || :query || '%'")
    fun searchFavorites(query: String): Single<List<Movie>>

    @Query("SELECT * FROM Favorite WHERE id = :movieId")
    fun checkFavoriteId(movieId: Long): Single<Movie?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(vararg movie: Movie): Single<List<Long>>

    @Delete
    fun deleteFavorite(vararg movie: Movie): Single<Int>
}