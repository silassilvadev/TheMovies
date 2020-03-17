package com.silas.themovies.ui.movies

import com.silas.themovies.model.BaseMoviesTest
import com.silas.themovies.model.dto.response.PagedMovies
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
        viewModelSUT = MoviesViewModel(repository)
    }

    @Test
    fun `Get all popular movies returns error`() = runBlockingTest {
        pauseDispatcher {
            //Given
            coEvery {
                repository.loadPopulars(any())
            } coAnswers { throw Throwable("Invalid data returned") }
        }

        //When
        viewModelSUT.getPopulars(-1)

        //Then
        assertTrue(viewModelSUT.errorLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Searching for popular movies returns error`() = runBlockingTest {
        pauseDispatcher {
            //Given
            coEvery {
                repository.searchPopulars(any())
            } coAnswers { throw Throwable("Invalid data returned") }
        }

        //When
        viewModelSUT.getPopulars(1, "Movie non-existent")

        //Then
        assertTrue(viewModelSUT.errorLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Get all favorite movies returns error`() = runBlockingTest {
        pauseDispatcher {
            //Given
            coEvery {
                repository.loadFavorites()
            } coAnswers { throw Throwable("Invalid data returned") }
        }

        //When
        viewModelSUT.getFavorites("")

        //Then
        assertTrue(viewModelSUT.errorLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Searching for favorite movies returns error`() = runBlockingTest {
        pauseDispatcher {
            //Given
            coEvery {
                repository.searchFavorites(any())
            } coAnswers { throw Throwable("Invalid data returned") }
        }

        //When
        viewModelSUT.getFavorites("Favorite non-existent")

        //Then
        assertTrue(viewModelSUT.errorLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Get all popular movies returns successful`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.loadPopulars(any())
            }.returns(PagedMovies(1, 10000, 500, arrayListOf()))
        }

        viewModelSUT.getPopulars(1, "")

        assertTrue(viewModelSUT.pagedMoviesLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Searching for all popular movies returns successful`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.searchPopulars(any())
            } returns PagedMovies(1, 10000, 500, arrayListOf())
        }

        viewModelSUT.getPopulars(1, "Parasita")

        assertTrue(viewModelSUT.pagedMoviesLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Get all favorite movies returns successful`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.loadFavorites()
            } returns arrayListOf()
        }

        viewModelSUT.getFavorites("")

        assertTrue(viewModelSUT.pagedMoviesLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Searching for favorite movies returns successful`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.searchFavorites(any())
            } returns arrayListOf()
        }

        viewModelSUT.getFavorites("Parasita")

        assertTrue(viewModelSUT.pagedMoviesLiveData.value != null)
        resumeDispatcher()
    }
}
