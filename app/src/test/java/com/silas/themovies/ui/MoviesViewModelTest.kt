package com.silas.themovies.ui

import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.model.BaseContactTest
import com.silas.themovies.model.dto.response.PagedListMovies
import com.silas.themovies.ui.main.movies.MoviesViewModel
import io.mockk.*
import kotlinx.coroutines.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesViewModelTest: BaseContactTest() {

    private lateinit var viewModelTest: MoviesViewModel

    private val repository = mockk<MoviesRepository>(relaxed = true)
    private val protocol = mockk<IProtocolError>(relaxed = true)

    @Before
    internal fun setUp() {
        viewModelTest = MoviesViewModel(repository, protocol)
    }

    @Test
    fun `Searching for popular movies returns error`() {
        //Given
        coRuleTest.pauseDispatcher()
        coEvery {
            repository.loadPopulars(any())
            repository.searchPopulars(any())
        }
            .coAnswers { throw Throwable("Invalid data returned") }

        //When
        viewModelTest.getPopulars(-1)
        viewModelTest.getPopulars(1, "Movie non-existent")

        //Then
        coRuleTest.resumeDispatcher()
        verifyAll {
            protocol.onResponseError(any())
            protocol.onResponseError(any())
        }
    }

    @Test
    fun `Searching for favorite movies returns error`() {
        //Given
        coRuleTest.pauseDispatcher()
        coEvery {
            repository.loadFavorites()
            repository.searchFavorites(any())
        } coAnswers { throw Throwable("Invalid data returned") }

        //When
        viewModelTest.getFavorites("")
        viewModelTest.getFavorites("Favorite non-existent")

        //Then
        coRuleTest.resumeDispatcher()
        verify {
            protocol.onResponseError(any())
            protocol.onResponseError(any())
        }
    }

    @Test
    fun `Searching for all popular movies returns successful`() {
        coRuleTest.pauseDispatcher()
        coEvery {
            repository.loadPopulars(any())
        } returns PagedListMovies(1, 10000, 500, arrayListOf())

        val popularsLiveData = viewModelTest.getPopulars(1)

        coRuleTest.resumeDispatcher()
        assertTrue(popularsLiveData.value != null)
    }

    @Test
    fun `Searching for popular movies returns successful`() {
        coRuleTest.pauseDispatcher()
        coEvery {
            repository.searchPopulars(any())
        } returns PagedListMovies(1, 10000, 500, arrayListOf())

        val searchPopularsLiveData =
            viewModelTest.getPopulars(1, "Existing Movie")

        coRuleTest.resumeDispatcher()
        assertTrue(searchPopularsLiveData.value != null)
    }

    @Test
    fun `Searching for all favorite movies returns successful`() {
        coRuleTest.pauseDispatcher()
        coEvery {
            repository.loadFavorites()
        } returns arrayListOf()

        val favoritesLiveData = viewModelTest.getFavorites("")

        coRuleTest.resumeDispatcher()
        assertTrue(favoritesLiveData.value != null)
    }

    @Test
    fun `Searching for favorite movies returns successful`() {
        coRuleTest.pauseDispatcher()
        coEvery {
            repository.searchFavorites(any())
        } returns arrayListOf()

        val searchFavoritesLiveData =
            viewModelTest.getFavorites("Existing Favorite")

        coRuleTest.resumeDispatcher()
        assertTrue(searchFavoritesLiveData.value != null)
    }
}
