package com.silas.themovies.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.model.dto.request.MovieDetailsDto
import com.silas.themovies.model.dto.request.PagedListMoviesDto
import com.silas.themovies.model.dto.response.PagedListMovies
import com.silas.themovies.ui.IProtocolError
import kotlinx.coroutines.*

/**
 * Request the details of the selected film, similar films, and option to make the film favorite or not.
 * The request made by the UI must be executed and returned when available.
 *
 * @param repository Single repository instance that will decide where to get the requested data
 * @param protocol Single instance of error message return protocol to UI
 * @property mutablePagedListMovies
 * @property mutableInsertFavorite
 * @property mutableDeleteFavorite
 * @property mutableMovie
 *  It is properties do basically the same thing as returning the response expected by the UI
 * @property viewModelScope Scope of request execution
 *
 * @author Silas at 24/02/2020
 */
class DetailsViewModel(private val repository: MoviesRepository,
                       private val protocol: IProtocolError): ViewModel() {

    private lateinit var mutableMovie: MutableLiveData<Movie>
    private lateinit var mutablePagedListMovies: MutableLiveData<PagedListMovies>
    private lateinit var mutableInsertFavorite: MutableLiveData<List<Long>>
    private lateinit var mutableDeleteFavorite: MutableLiveData<Int>

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun loadDetails(movieId: Long): MutableLiveData<Movie> {
        mutableMovie = MutableLiveData()
        val movieDetailsDto = MovieDetailsDto(movieId)

        viewModelScope.launch {
            runCatching {
                async {
                    repository.loadDetails(movieDetailsDto)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: it.localizedMessage ?: "")
            }.onSuccess {
                mutableMovie.postValue(it.await())
            }
        }
        return mutableMovie
    }

    fun loadRelated(page: Int,
                    movieId: Long,
                    language: String = PagedListMoviesDto.PT_BR): MutableLiveData<PagedListMovies> {
        mutablePagedListMovies = MutableLiveData()
        val pagedListMoviesDto = PagedListMoviesDto(page, language, movieId = movieId)

        viewModelScope.launch {
            runCatching {
                async { repository.loadRelated(pagedListMoviesDto) }
            }.onFailure {
                protocol.onResponseError(it.message ?: it.localizedMessage ?: "")
            }.onSuccess {
                mutablePagedListMovies.postValue(it.await())
            }
        }
        return mutablePagedListMovies
    }

    fun saveFavorite(vararg movie: Movie): MutableLiveData<List<Long>> {
        mutableInsertFavorite = MutableLiveData()

        viewModelScope.launch {
            runCatching {
                async {
                    repository.insertFavorite(*movie)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: it.localizedMessage ?: "")
            }.onSuccess {
                mutableInsertFavorite.postValue(it.await())
            }
        }
        return mutableInsertFavorite
    }

    fun removeFavorite(vararg movie: Movie): MutableLiveData<Int> {
        mutableDeleteFavorite = MutableLiveData()

        viewModelScope.launch {
            runCatching {
                async {
                    repository.deleteFavorite(*movie)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: it.localizedMessage ?: "")
            }.onSuccess {
                mutableDeleteFavorite.postValue(it.await())
            }
        }
        return mutableDeleteFavorite
    }

    fun loadFavoriteId(movieId: Long): MutableLiveData<Movie> {
        mutableMovie = MutableLiveData()

        viewModelScope.launch {
            runCatching {
                async {
                    repository.loadFavoriteId(movieId)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: it.localizedMessage ?: "")
            }.onSuccess { itDeferred ->
                itDeferred.await().let {
                    mutableMovie.postValue(it)
                }
            }
        }
        return mutableMovie
    }
}