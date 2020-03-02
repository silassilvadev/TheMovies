package com.silas.themovies.ui.main.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.model.dto.request.PagedListMoviesDto
import com.silas.themovies.model.dto.request.PagedListMoviesDto.Companion.PT_BR
import com.silas.themovies.model.dto.response.PagedListMovies
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.ui.IProtocolError
import kotlinx.coroutines.*

/**
 * Mediator responsible for requesting the data of popular and favorite films requested by the UI,
 * and returning them when they are available.
 *
 * @param repository Single repository instance that will decide where to get the requested data
 * @param protocol Single instance of error message return protocol to UI
 * @property mutablePagedListMovies The properties of this class do basically the same thing
 * as returning the response expected by the UI
 * @property viewModelScope Scope of request execution
 *
 * @author Silas at 22/02/2020
 */
class MoviesViewModel(private val repository: MoviesRepository,
                      private val protocol: IProtocolError): ViewModel() {

    private lateinit var mutablePagedListMovies: MutableLiveData<PagedListMovies>

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun getPopulars(page: Int, query: String = "") =
        if (query.isBlank()) loadPopulars(page) else searchPopulars(query, page)

    fun getFavorites(query: String) =
        if (query.isBlank()) loadFavorites() else searchFavorites(query)

    private fun loadPopulars(page: Int, language: String = PT_BR): MutableLiveData<PagedListMovies> {
        mutablePagedListMovies = MutableLiveData()
        val pagedListMoviesDto = PagedListMoviesDto(page, language)

        viewModelScope.launch {
            runCatching {
                repository.loadPopulars(pagedListMoviesDto)
            }.onFailure {
                protocol.onResponseError(it.message ?: it.localizedMessage ?: "")
            }.onSuccess {
                mutablePagedListMovies.postValue(it)
            }
        }

        return mutablePagedListMovies
    }

    private fun searchPopulars(query: String, page: Int): MutableLiveData<PagedListMovies> {
        mutablePagedListMovies = MutableLiveData()
        val pagedListMoviesDto = PagedListMoviesDto(page, search = query)

        viewModelScope.launch {
            runCatching {
                repository.searchPopulars(pagedListMoviesDto)
            }.onFailure {
                protocol.onResponseError(it.message ?: it.localizedMessage ?: "")
            }.onSuccess {
                mutablePagedListMovies.postValue(it)
            }
        }
        return mutablePagedListMovies
    }

    private fun loadFavorites(): MutableLiveData<PagedListMovies> {
        mutablePagedListMovies = MutableLiveData()

        viewModelScope.launch {
            runCatching {
                repository.loadFavorites()
            }.onFailure {
                protocol.onResponseError(it.message ?: it.localizedMessage ?: "")
            }.onSuccess {
                val movies = it as ArrayList<Movie>
                val pagedListMovies = PagedListMovies(totalResults = movies.size, results = movies)
                mutablePagedListMovies.postValue(pagedListMovies)
            }
        }
        return mutablePagedListMovies
    }

    private fun searchFavorites(query: String): MutableLiveData<PagedListMovies> {
        mutablePagedListMovies = MutableLiveData()

        viewModelScope.launch {
            runCatching {
               repository.searchFavorites(query)
            }.onFailure {
                protocol.onResponseError(it.message ?: it.localizedMessage ?: "")
            }.onSuccess {
                val pagedListMovies = PagedListMovies(totalResults = it.size, results = it as ArrayList<Movie>)
                mutablePagedListMovies.postValue(pagedListMovies)
            }
        }
        return mutablePagedListMovies
    }
}