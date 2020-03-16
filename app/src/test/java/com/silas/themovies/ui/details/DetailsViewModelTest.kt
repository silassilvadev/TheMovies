package com.silas.themovies.ui.details

import androidx.lifecycle.MutableLiveData
import com.silas.themovies.model.BaseMoviesTest
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.model.dto.response.PagedMovies
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
        viewModelSUT = DetailsViewModel(repository, protocol)
    }

    @Test
    fun `Details not found`() = runBlockingTest {
        pauseDispatcher {
            coEvery { repository.loadDetailsAsync(any()) }
                .throws(Throwable("Invalid Movie Id"))

            viewModelSUT.loadDetails(-1)
        }

        verify { protocol.onResponseError(any()) }
    }

    @Test
    fun `Related not found`() = runBlockingTest {
        pauseDispatcher {
            coEvery {
                repository.loadRelatedAsync(any())
            }.throws(Throwable("Invalid data returned"))

            viewModelSUT.loadRelated(1,-1)
        }

        verify { protocol.onResponseError(any()) }
    }

    @Test
    fun `Errors updating favorites`() = runBlockingTest {
        pauseDispatcher {
            coEvery { repository.loadFavoriteIdAsync(any()) }
                .throws(Throwable("Invalid favorites"))
            coEvery { repository.insertFavoriteAsync(any()) }
                .throws(Throwable("Invalid insert favorite"))
            coEvery { repository.deleteFavoriteAsync(any()) }
                .throws(Throwable("Invalid delete favorite"))

            viewModelSUT.loadFavoriteId(-1)
            viewModelSUT.saveFavorite(mockk(relaxed = true))
            viewModelSUT.removeFavorite(mockk(relaxed = true))
        }

        verifyAll {
            protocol.onResponseError(any())
            protocol.onResponseError(any())
            protocol.onResponseError(any())
        }
    }

    @Test
    fun `Success load details movie`() = runBlockingTest {
        val detailsLiveData = MutableLiveData<Movie>()

        pauseDispatcher {
            coEvery { repository.loadDetailsAsync(any()) } returns mockk(relaxed = true)

            detailsLiveData.value = viewModelSUT.loadDetails(1).value
        }

        assertTrue(detailsLiveData.value != null)
    }

    @Test
    fun `Success load related movies`() = runBlockingTest {
        val relatedLiveData = MutableLiveData<PagedMovies>()

        pauseDispatcher {
            coEvery { repository.loadRelatedAsync(any()) } returns mockk(relaxed = true)

            relatedLiveData.value = viewModelSUT.loadRelated(1, 1).value
        }

        assertTrue(relatedLiveData.value != null)
    }

    @Test
    fun `Success updating favorites`() = runBlockingTest {
        val favoriteLiveData = MutableLiveData<Movie>()
        val insertedLiveData = MutableLiveData<List<Long>>()
        val removedLiveData = MutableLiveData<Int>()

        pauseDispatcher {
            coEvery { repository.loadFavoriteIdAsync(any()) } returns mockk(relaxed = true)
            coEvery { repository.insertFavoriteAsync(any()) } returns mockk(relaxed = true)
            coEvery { repository.deleteFavoriteAsync(any()) } returns mockk(relaxed = true)

            favoriteLiveData.value = viewModelSUT.loadFavoriteId(1).value
            insertedLiveData.value = viewModelSUT.saveFavorite(mockk(relaxed = true)).value
            removedLiveData.value = viewModelSUT.removeFavorite(mockk(relaxed = true)).value
        }

        assertTrue(favoriteLiveData.value != null)
        assertTrue(insertedLiveData.value != null)
        assertTrue(removedLiveData.value != null)
    }
}
