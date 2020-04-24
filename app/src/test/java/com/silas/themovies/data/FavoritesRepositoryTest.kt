package com.silas.themovies.data

import com.silas.themovies.data.repository.favorites.FavoritesRepository
import com.silas.themovies.data.repository.favorites.FavoritesRepositoryImpl
import com.silas.themovies.data.sources.local.dao.FavoritesDao
import com.silas.themovies.model.entity.PagedMovies
import com.silas.themovies.utils.BaseMoviesTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import io.reactivex.Maybe
import io.reactivex.MaybeObserver
import io.reactivex.Single
import io.reactivex.internal.operators.maybe.MaybeCallbackObserver
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FavoritesRepositoryTest: BaseMoviesTest() {

    private lateinit var favoritesRepository: FavoritesRepository
    private val favoritesDao = mockk<FavoritesDao>(relaxed = true)

    @Before
    fun setup() {
        this.favoritesRepository = FavoritesRepositoryImpl(favoritesDao)
    }

    @Test
    fun `Get all favorites error`() {
        every {
            favoritesDao.loadFavorites()
        } returns Single.error(Throwable("Ocorreu um erro inexperado"))

        favoritesRepository
            .loadFavorites()
            .test()
            .assertError(Throwable::class.java)
    }

    @Test
    fun `Get all favorites success`() {
        every {
            favoritesDao.loadFavorites()
        } returns Single.just(mockk())

        favoritesRepository
            .loadFavorites()
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `Search favorites error`() {
        every {
            favoritesDao.searchFavorites(any())
        } returns Single.error(Throwable("Ocorreu um erro inexperado"))

        favoritesRepository
            .searchFavorites("abc")
            .test()
            .assertError(Throwable::class.java)
            .assertNotComplete()
    }

    @Test
    fun `Search favorites success`() {
        every {
            favoritesDao.searchFavorites(any())
        } returns Single.just(mockk())

        favoritesRepository
            .searchFavorites("abc")
            .test()
            .assertNoErrors()
            .assertComplete()
    }
}