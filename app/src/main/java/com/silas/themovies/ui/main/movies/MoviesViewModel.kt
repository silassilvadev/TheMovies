package com.silas.themovies.ui.main.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.model.dto.request.PagedListMoviesDto
import com.silas.themovies.model.dto.request.PagedListMoviesDto.Companion.PT_BR
import com.silas.themovies.model.dto.response.PagedListMovies
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.ui.IProtocolError
import kotlinx.coroutines.*

class MoviesViewModel(private val repository: MoviesRepository,
                      private val protocol: IProtocolError): ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val mutablePagedListMovies = MutableLiveData<PagedListMovies>()
    private val mutableFavorites = MutableLiveData<List<Movie>>()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getPopulars(page: Int, query: String = ""): MutableLiveData<PagedListMovies> {
        return if (query.isBlank()) loadPopulars(page) else searchPopulars(query, page)
    }

    fun getFavorites(query: String): MutableLiveData<List<Movie>> {
        return if (query.isBlank()) loadFavorites() else searchFavorites(query)
    }


    fun loadPopulars(page: Int, language: String = PT_BR): MutableLiveData<PagedListMovies> {
        val pagedListMoviesDto = PagedListMoviesDto(page, language)

        uiScope.launch {
            runCatching {
                async {
                    repository.loadPopulars(pagedListMoviesDto)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: "")
            }.onSuccess {
                mutablePagedListMovies.postValue(it.await())
            }
        }

        return mutablePagedListMovies
    }

    fun searchPopulars(query: String, page: Int, language: String = PT_BR): MutableLiveData<PagedListMovies> {
        val pagedListMoviesDto = PagedListMoviesDto(page, language, query)
        uiScope.launch {
            runCatching {
                async {
                    repository.searchMovie(pagedListMoviesDto)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: "Erro")
            }.onSuccess {
                mutablePagedListMovies.postValue(it.await())
            }
        }
        return mutablePagedListMovies
    }

    private fun loadFavorites(): MutableLiveData<List<Movie>> {
        uiScope.launch {
            kotlin.runCatching {
                async {
                    repository.loadFavorites()
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: "Erro")
            }.onSuccess {
                mutableFavorites.postValue(it.await())
            }
        }
        return mutableFavorites
    }

    private fun searchFavorites(query: String): MutableLiveData<List<Movie>> {
        uiScope.launch {
            kotlin.runCatching {
                async {
                    repository.searchFavorites(query)
                }
            }.onFailure {
                protocol.onResponseError(it.message ?: "Erro")
            }.onSuccess {
                mutableFavorites.postValue(it.await())
            }
        }
        return mutableFavorites
    }
}