package com.silas.themovies.model.dto.request

import java.io.Serializable

data class MovieDetailsDto(val idMovie: Long): BaseMoviesDto(), Serializable