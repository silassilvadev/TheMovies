package com.silas.themovies.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.dto.request.MovieDetailsDto
import com.silas.themovies.model.dto.request.PagedListMoviesDto
import com.silas.themovies.model.dto.response.MovieDetails
import com.silas.themovies.model.dto.response.PagedListMovies
import com.silas.themovies.ui.IProtocolError
import kotlinx.coroutines.*

class DetailsViewModel(private val repository: MoviesRepository,
                       private val protocol: IProtocolError): ViewModel() {

    private val detailViewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + detailViewModelJob)

    private val mutablePagedListMovies = MutableLiveData<PagedListMovies>()
    private val mutableMovieDetails = MutableLiveData<MovieDetails>()
    private val mutableInsertFavorite = MutableLiveData<List<Long>>()
    private val mutableDeleteFavorite = MutableLiveData<Int>()
    private val mutableFavoriteIdFavorite = MutableLiveData<Movie>()

    override fun onCleared() {
        super.onCleared()
        detailViewModelJob.cancel()
    }

    fun loadDetails(idMovie: Long, language: String = PagedListMoviesDto.PT_BR): MutableLiveData<MovieDetails> {
        val movieDetailsDto = MovieDetailsDto(idMovie, language)

        uiScope.launch {
            runCatching {
                async {
                    repository.loadDetails(movieDetailsDto)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: "")
            }.onSuccess {
                mutableMovieDetails.postValue(it.await())
            }
        }
        return mutableMovieDetails
    }

    fun searchRelated(page: Int,
                      movieId: Long,
                      language: String = PagedListMoviesDto.PT_BR): MutableLiveData<PagedListMovies> {
        val pagedListMoviesDto = PagedListMoviesDto(page, language, movieId = movieId)

        uiScope.launch {
            runCatching {
                async {
                    repository.searchRelated(pagedListMoviesDto)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: "Error")
            }.onSuccess {
                mutablePagedListMovies.postValue(it.await())
            }
        }
        return mutablePagedListMovies
    }

    fun saveFavorite(vararg movie: Movie): MutableLiveData<List<Long>> {
        uiScope.launch {
            runCatching {
                async {
                    repository.insertFavorite(*movie)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: "Error")
            }.onSuccess {
                mutableInsertFavorite.postValue(it.await())
            }
        }
        return mutableInsertFavorite
    }

    fun removeFavorite(vararg movie: Movie): MutableLiveData<Int> {
        uiScope.launch {
            runCatching {
                async {
                    repository.deleteFavorite(*movie)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: "Error")
            }.onSuccess {
                mutableDeleteFavorite.postValue(it.await())
            }
        }
        return mutableDeleteFavorite
    }

    fun loadFavoriteId(movieId: Long): MutableLiveData<Movie> {
        uiScope.launch {
            runCatching {
                async {
                    repository.loadFavoriteId(movieId)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: "Error")
            }.onSuccess { itDeferred ->
                itDeferred.await().let {
                    mutableFavoriteIdFavorite.postValue(it)
                }
            }
        }
        return mutableFavoriteIdFavorite
    }
}