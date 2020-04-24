package com.silas.themovies.data

import com.silas.themovies.data.repository.details.DetailsRepository
import com.silas.themovies.data.repository.details.DetailsRepositoryImpl
import com.silas.themovies.data.sources.local.dao.FavoritesDao
import com.silas.themovies.data.sources.remote.service.DetailsService
import com.silas.themovies.utils.BaseMoviesTest
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class DetailsRepositoryTest: BaseMoviesTest() {

    private lateinit var detailsRepository: DetailsRepository
    private val favoritesDao = mockk<FavoritesDao>()
    private val apiKey = "apiKeyMockk"
    private val language = "pt-BR"
    private val moviesService = mockk<DetailsService>(relaxed = true)

    @Before
    fun setup() {
        this.detailsRepository = DetailsRepositoryImpl(moviesService, favoritesDao, apiKey, language)
    }

    @Test
    fun `Get movie details error`() {
        every {
            moviesService.loadDetails(any(), any(), any())
        } returns Single.error(Throwable())

        detailsRepository
            .loadDetails(-1L)
            .test()
            .assertError(Throwable::class.java)
            .assertNotComplete()
    }

    @Test
    fun `Get movie details success`() {
        every {
            moviesService.loadDetails(any(), any(), any())
        } returns Single.just(mockk())

        detailsRepository
            .loadDetails(12345L)
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `Get movie related error`() {
        every {
            moviesService.loadRelated(any(), any(), any(), any())
        } returns Single.error(Throwable())

        detailsRepository
            .loadRelated(1, -1)
            .test()
            .assertError(Throwable::class.java)
            .assertNotComplete()
    }

    @Test
    fun `Get movie related success`() {
        every {
            moviesService.loadRelated(any(), any(), any(), any())
        } returns Single.just(mockk())

        detailsRepository
            .loadRelated(1, 12345L)
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `Check if movie is favorite error`() {
        every {
            favoritesDao.checkFavoriteId(any())
        } returns Single.error(Throwable())

        detailsRepository
            .checkFavoriteId(-1L)
            .test()
            .assertError(Throwable::class.java)
            .assertNotComplete()
    }

    @Test
    fun `Check if movie is favorite success`() {
        every {
            favoritesDao.checkFavoriteId(any())
        } returns Single.just(mockk())

        detailsRepository
            .checkFavoriteId(12345L)
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `Insert movie in favorites table error`() {
        every {
            favoritesDao.insertFavorite(any())
        } returns Single.error(Throwable())

        detailsRepository
            .insertFavorite(mockk())
            .test()
            .assertError(Throwable::class.java)
            .assertNotComplete()
    }

    @Test
    fun `Insert movie in favorites table success`() {
        every {
            favoritesDao.insertFavorite(any())
        } returns Single.just(mockk())

        detailsRepository
            .insertFavorite(mockk())
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `Remove movie in favorites table error`() {
        every {
            favoritesDao.deleteFavorite(any())
        } returns Single.error(Throwable())

        detailsRepository
            .deleteFavorite(mockk())
            .test()
            .assertError(Throwable::class.java)
            .assertNotComplete()
    }

    @Test
    fun `Remove movie in favorites table success`() {
        every {
            favoritesDao.deleteFavorite(any())
        } returns Single.just(mockk())

        detailsRepository
            .deleteFavorite(mockk())
            .test()
            .assertNoErrors()
            .assertComplete()
    }
}