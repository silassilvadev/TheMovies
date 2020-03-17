package com.silas.themovies.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.model.dto.request.MovieDetailsDto
import com.silas.themovies.model.dto.request.PagedListMoviesDto
import com.silas.themovies.model.dto.response.PagedMovies
import com.silas.themovies.ui.LoadingState
import kotlinx.coroutines.*

/**
 * Request the details of the selected film, similar films, and option to make the film favorite or not.
 * The request made by the UI must be executed and returned when available.
 *
 * @param repository Single repository instance that will decide where to get the requested data
 * @property pagedRelatedMutableLiveData
 * @property updateFavoritesLiveData
 * @property movieDetailsMutableLiveData
 *  It is properties do basically the same thing as returning the response expected by the UI
 * @property viewModelScope Scope of request execution
 *
 * @author Silas at 24/02/2020
 */
class DetailsViewModel(private val repository: MoviesRepository): ViewModel() {

    private val movieDetailsMutableLiveData = MutableLiveData<Movie>()
    val movieDetailsLiveData: LiveData<Movie> get()= movieDetailsMutableLiveData
    
    private val pagedRelatedMutableLiveData = MutableLiveData<PagedMovies>()
    val pagedRelatedLiveData: LiveData<PagedMovies> get() = pagedRelatedMutableLiveData

    private val updateFavoritesMutableLiveData = MutableLiveData<Boolean>()
    val updateFavoritesLiveData: LiveData<Boolean> get() = updateFavoritesMutableLiveData

    private val errorMutableLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = errorMutableLiveData

    private val loadingMutableLiveData = MutableLiveData<LoadingState>()
    val loadingLiveData: LiveData<LoadingState> get() = loadingMutableLiveData

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun loadDetails(movieId: Long) {
        val movieDetailsDto = MovieDetailsDto(movieId)

        viewModelScope.launch {
            runCatching {
                loadingMutableLiveData.value = LoadingState.SHOW
                repository.loadDetailsAsync(movieDetailsDto)
            }.onFailure {
                loadingMutableLiveData.value = LoadingState.HIDE
                errorMutableLiveData.value = it.message ?: it.localizedMessage ?: ""
            }.onSuccess {
                loadingMutableLiveData.value = LoadingState.HIDE
                movieDetailsMutableLiveData.postValue(it.await())
            }
        }
    }

    fun loadRelated(page: Int, movieId: Long) {
        val pagedListMoviesDto = PagedListMoviesDto(page, movieId = movieId)

        viewModelScope.launch {
            runCatching {
                loadingMutableLiveData.value = LoadingState.SHOW
                repository.loadRelatedAsync(pagedListMoviesDto)
            }.onFailure {
                loadingMutableLiveData.value = LoadingState.HIDE
                errorMutableLiveData.value = it.message ?: it.localizedMessage ?: ""
            }.onSuccess {
                loadingMutableLiveData.value = LoadingState.HIDE
                pagedRelatedMutableLiveData.postValue(it.await())
            }
        }
    }

    fun updateFavorite(vararg movie: Movie) {
        movie.forEach {
            if (it.hasFavorite) saveFavorite(*movie) else removeFavorite(*movie)
        }
    }

    private fun saveFavorite(vararg movie: Movie) {
        viewModelScope.launch {
            runCatching {
                loadingMutableLiveData.value = LoadingState.SHOW
                repository.insertFavoriteAsync(*movie)
            }.onFailure {
                loadingMutableLiveData.value = LoadingState.HIDE
                errorMutableLiveData.value = it.message ?: it.localizedMessage ?: ""
            }.onSuccess {
                loadingMutableLiveData.value = LoadingState.HIDE
                updateFavoritesMutableLiveData.postValue(!it.await().isNullOrEmpty())
            }
        }
    }

    private fun removeFavorite(vararg movie: Movie) {
        viewModelScope.launch {
            runCatching {
                loadingMutableLiveData.value = LoadingState.SHOW
                repository.deleteFavoriteAsync(*movie)
            }.onFailure {
                loadingMutableLiveData.value = LoadingState.HIDE
                errorMutableLiveData.value = it.message ?: it.localizedMessage ?: ""
            }.onSuccess {
                loadingMutableLiveData.value = LoadingState.HIDE
                updateFavoritesMutableLiveData.postValue(it.await() > 0)
            }
        }
    }

    fun loadFavoriteId(movieId: Long) {
        viewModelScope.launch {
            runCatching {
                loadingMutableLiveData.value = LoadingState.SHOW
                repository.loadFavoriteIdAsync(movieId)
            }.onFailure {
                loadingMutableLiveData.value = LoadingState.HIDE
                errorMutableLiveData.value = it.message ?: it.localizedMessage ?: ""
            }.onSuccess {
                loadingMutableLiveData.value = LoadingState.HIDE
                movieDetailsMutableLiveData.postValue(it.await())
            }
        }
    }
}