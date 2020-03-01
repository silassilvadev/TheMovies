package com.silas.themovies

import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.model.dto.request.PagedListMoviesDto
import com.silas.themovies.model.dto.response.PagedListMovies
import com.silas.themovies.ui.IProtocolError
import com.silas.themovies.ui.main.movies.MoviesViewModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MoviesViewModelTest {

    private lateinit var viewModelTest: MoviesViewModel

    private val repository = mockk<MoviesRepository>(relaxed = true)
    private val protocol = mockk<IProtocolError>(relaxed = true)

    private val pagedListMoviesDto = slot<PagedListMoviesDto>()
    private val pagedListMovies = slot<PagedListMovies>()

    @Before
    internal fun setUp() {
        viewModelTest = spyk(MoviesViewModel(repository, protocol))
    }

    @Test
    fun `return message error for view`() {
        runBlockingTest {
            runCatching {
                repository.loadPopulars(PagedListMoviesDto(-1))
            }.onFailure {
                protocol.onResponseError(it.message ?: "Página inválida")
            }
        }

        verify {
            protocol.onResponseError(any())
        }
    }
}