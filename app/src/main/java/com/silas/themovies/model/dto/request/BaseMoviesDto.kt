package com.silas.themovies.model.dto.request

import com.silas.themovies.data.remote.client.ClientService.Companion.API_KEY

/**
 * Object model for requests that need to pass an apiKey
 *
 * @param apiKey Key of the access in API
 * @param language Language of movies searched
 *
 * @author Silas at 23/02/2020
 */
abstract class BaseMoviesDto(val apiKey: String = API_KEY)