package com.silas.themovies.model.dto.request

import com.silas.themovies.data.remote.client.ClientService.Companion.API_KEY

abstract class BaseMoviesDto(val apiKey: String = API_KEY)