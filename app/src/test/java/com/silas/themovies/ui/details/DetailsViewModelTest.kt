package com.silas.themovies.ui.details

import com.silas.themovies.model.BaseMoviesTest
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.ui.detail.DetailsViewModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest: BaseMoviesTest() {

    private lateinit var viewModelSUT: DetailsViewModel

    @Before
    fun setUp() {
        viewModelSUT = DetailsViewModel(repository)
    }

    @Test
    fun `Details not found`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.loadDetailsAsync(any())
            }.throws(Throwable("Invalid Movie Id"))
        }

        viewModelSUT.loadDetails(-1)

        assertTrue(viewModelSUT.errorLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Related not found`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.loadRelatedAsync(any())
            }.throws(Throwable("Invalid data returned"))
        }

        viewModelSUT.loadRelated(1,-1)

        assertTrue(viewModelSUT.errorLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Errors verify is favorite`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.loadFavoriteIdAsync(any())
            }.throws(Throwable("Invalid favorites"))
        }

        viewModelSUT.loadFavoriteId(-1)

        assertTrue(viewModelSUT.errorLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Errors insert favorites`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.insertFavoriteAsync(any())
            }.throws(Throwable("Invalid insert favorite"))
        }

        val movieInsert = mockk<Movie>(relaxed = true).apply {
            hasFavorite = true
        }

        viewModelSUT.updateFavorite(movieInsert)

        assertTrue(viewModelSUT.errorLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Errors remove favorites`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.deleteFavoriteAsync(any())
            }.throws(Throwable("Invalid remove favorite"))
        }

        val movieRemove = mockk<Movie>(relaxed = true).apply {
            hasFavorite = false
        }

        viewModelSUT.updateFavorite(movieRemove)

        assertTrue(viewModelSUT.errorLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Success load details movie`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.loadDetailsAsync(any())
            }.returns(mockk(relaxed = true))
        }

        viewModelSUT.loadDetails(1)

        assertTrue(viewModelSUT.movieDetailsLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Success load related movies`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.loadRelatedAsync(any())
            } returns mockk(relaxed = true)
        }

        viewModelSUT.loadRelated(1, 1)

        assertTrue(viewModelSUT.pagedRelatedLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Success verify is favorite`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.loadFavoriteIdAsync(any())
            } returns mockk(relaxed = true)
        }

        viewModelSUT.loadFavoriteId(1)

        assertTrue(viewModelSUT.movieDetailsLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Success insert favorite`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.insertFavoriteAsync(any()).await()
            } returns listOf(1L)
        }

        val movieInsert = mockk<Movie> {
            every { hasFavorite } returns true
        }
        viewModelSUT.updateFavorite(movieInsert)

        assertTrue(viewModelSUT.updateFavoritesLiveData.value != null)
        resumeDispatcher()
    }

    @Test
    fun `Success remove favorite`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.deleteFavoriteAsync(any()).await()
            } returns 1
        }

        val movieInsert = mockk<Movie> {
            every { hasFavorite } returns false
        }
        viewModelSUT.updateFavorite(movieInsert)

        assertTrue(viewModelSUT.updateFavoritesLiveData.value != null)
        resumeDispatcher()
    }
}
