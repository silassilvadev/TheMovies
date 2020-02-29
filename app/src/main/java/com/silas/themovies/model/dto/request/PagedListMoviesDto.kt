package com.silas.themovies.model.dto.request

import java.io.Serializable

data class PagedListMoviesDto(var page: Int = 1,
                              val language: String = PT_BR,
                              var search: String? = null,
                              var movieId: Long = -1): BaseMoviesDto(), Serializable {

    companion object {
        const val PT_BR = "pt-BR"
    }
}