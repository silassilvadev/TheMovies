package com.silas.themovies.model.dto.request

import java.io.Serializable

data class MovieDetailsDto(val idMovie: Long,
                           val language: String = PT_BR): BaseMoviesDto(), Serializable {

    companion object {
        const val PT_BR = "pt-BR"
    }
}