package com.silas.themovies.model.dto.request

import java.io.Serializable

data class PagedListMoviesDto(var page: Int = 1,
                              var search: String? = null,
                              var movieId: Long = -1): BaseMoviesDto(), Serializable