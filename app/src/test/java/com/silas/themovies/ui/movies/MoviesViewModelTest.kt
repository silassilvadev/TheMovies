package com.silas.themovies.ui.movies

import androidx.lifecycle.MutableLiveData
import com.silas.themovies.model.BaseMoviesTest
import com.silas.themovies.model.dto.response.PagedListMovies
import com.silas.themovies.ui.main.movies.MoviesViewModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesViewModelTest: BaseMoviesTest() {

    private lateinit var viewModelSUT: MoviesViewModel

    @Before
    internal fun setUp() {
        viewModelSUT = MoviesViewModel(repository, protocol)
    }

    @Test
    fun `Searching for popular movies returns error`() = runBlockingTest {
        pauseDispatcher {
            //Given
            coEvery {
                repository.loadPopulars(any())
                repository.searchPopulars(any())
            } coAnswers { throw Throwable("Invalid data returned") }

            //When
            viewModelSUT.getPopulars(-1)
            viewModelSUT.getPopulars(1, "Movie non-existent")
        }

        //Then
        verifyAll {
            protocol.onResponseError(any())
            protocol.onResponseError(any())
        }
    }

    @Test
    fun `Searching for favorite movies returns error`() = runBlockingTest {
        pauseDispatcher {
            //Given
            coEvery {
                repository.loadFavorites()
                repository.searchFavorites(any())
            } coAnswers { throw Throwable("Invalid data returned") }

            //When
            viewModelSUT.getFavorites("")
            viewModelSUT.getFavorites("Favorite non-existent")
        }

        //Then
        verify {
            protocol.onResponseError(any())
            protocol.onResponseError(any())
        }
    }

    @Test
    fun `Searching for all popular movies returns successful`() = runBlockingTest {
        val popularsLiveData = MutableLiveData<PagedListMovies>()
        pauseDispatcher {
            coEvery {
                repository.loadPopulars(any())
            } returns PagedListMovies(1, 10000, 500, arrayListOf())

            popularsLiveData.value = viewModelSUT.getPopulars(1).value
        }

        assertTrue(popularsLiveData.value != null)
    }

    @Test
    fun `Searching for popular movies returns successful`() = runBlockingTest {
        val searchPopularsLiveData = MutableLiveData<PagedListMovies>()
        pauseDispatcher {
            coEvery {
                repository.searchPopulars(any())
            } returns PagedListMovies(1, 10000, 500, arrayListOf())

            searchPopularsLiveData.value = viewModelSUT.getPopulars(1, "Existing Movie").value
        }

        assertTrue(searchPopularsLiveData.value != null)
    }

    @Test
    fun `Searching for all favorite movies returns successful`() = runBlockingTest {
        val favoritesLiveData = MutableLiveData<PagedListMovies>()
            pauseDispatcher {
            coEvery {
                repository.loadFavorites()
            } returns arrayListOf()

            favoritesLiveData.value = viewModelSUT.getFavorites("").value
        }

        assertTrue(favoritesLiveData.value != null)
    }

    @Test
    fun `Searching for favorite movies returns successful`() = runBlockingTest {
        val searchFavoritesLiveData = MutableLiveData<PagedListMovies>()
        pauseDispatcher {
            coEvery {
                repository.searchFavorites(any())
            } returns arrayListOf()

            searchFavoritesLiveData.value = viewModelSUT.getFavorites("Existing Favorite").value
        }

        assertTrue(searchFavoritesLiveData.value != null)
    }
}
