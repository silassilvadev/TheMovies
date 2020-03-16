package com.silas.themovies.ui.main.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.model.dto.request.PagedListMoviesDto
import com.silas.themovies.model.dto.response.PagedMovies
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.ui.LoadingState
import kotlinx.coroutines.*

/**
 * Mediator responsible for requesting the data of popular and favorite films requested by the UI,
 * and returning them when they are available.
 *
 * @param repository Single repository instance that will decide where to get the requested data
 * @property pagedMoviesMutableLiveData The properties of this class do basically the same thing
 * as returning the response expected by the UI
 * @property viewModelScope Scope of request execution
 *
 * @author Silas at 22/02/2020
 */
class MoviesViewModel(private val repository: MoviesRepository): ViewModel() {

    private val pagedMoviesMutableLiveData = MutableLiveData<PagedMovies>()
    val pagedMoviesLiveData: LiveData<PagedMovies> get() = pagedMoviesMutableLiveData

    private val errorMutableLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = errorMutableLiveData

    private val loadingMutableLiveData = MutableLiveData<LoadingState>()
    val loadingLiveData: LiveData<LoadingState> get() = loadingMutableLiveData

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun getPopulars(page: Int, query: String = "") =
        if (query.isBlank()) loadPopulars(page) else searchPopulars(query, page)

    fun getFavorites(query: String) =
        if (query.isBlank()) loadFavorites() else searchFavorites(query)

    private fun loadPopulars(page: Int) {
        val pagedListMoviesDto = PagedListMoviesDto(page)

        viewModelScope.launch {
            runCatching {
                loadingMutableLiveData.value = LoadingState.SHOW
                repository.loadPopulars(pagedListMoviesDto)
            }.onFailure {
                loadingMutableLiveData.value = LoadingState.HIDE
                errorMutableLiveData.value = it.message ?: it.localizedMessage ?: ""
            }.onSuccess {
                loadingMutableLiveData.value = LoadingState.HIDE
                pagedMoviesMutableLiveData.postValue(it)
            }
        }
    }

    private fun searchPopulars(query: String, page: Int) {
        val pagedListMoviesDto = PagedListMoviesDto(page, search = query)

        viewModelScope.launch {
            runCatching {
                loadingMutableLiveData.value = LoadingState.SHOW
                repository.searchPopulars(pagedListMoviesDto)
            }.onFailure {
                loadingMutableLiveData.value = LoadingState.HIDE
                errorMutableLiveData.value = it.message ?: it.localizedMessage ?: ""
            }.onSuccess {
                loadingMutableLiveData.value = LoadingState.HIDE
                pagedMoviesMutableLiveData.postValue(it)
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            runCatching {
                loadingMutableLiveData.value = LoadingState.SHOW
                repository.loadFavorites()
            }.onFailure {
                loadingMutableLiveData.value = LoadingState.HIDE
                errorMutableLiveData.value = it.message ?: it.localizedMessage ?: ""
            }.onSuccess {
                loadingMutableLiveData.value = LoadingState.HIDE
                val movies = it as ArrayList<Movie>
                val pagedListMovies = PagedMovies(totalResults = movies.size, results = movies)
                pagedMoviesMutableLiveData.postValue(pagedListMovies)
            }
        }
    }

    private fun searchFavorites(query: String) {
        viewModelScope.launch {
            runCatching {
                loadingMutableLiveData.value = LoadingState.SHOW
               repository.searchFavorites(query)
            }.onFailure {
                loadingMutableLiveData.value = LoadingState.HIDE
                errorMutableLiveData.value = it.message ?: it.localizedMessage ?: ""
            }.onSuccess {
                loadingMutableLiveData.value = LoadingState.HIDE
                val pagedListMovies = PagedMovies(totalResults = it.size, results = it as ArrayList<Movie>)
                pagedMoviesMutableLiveData.postValue(pagedListMovies)
            }
        }
    }
}