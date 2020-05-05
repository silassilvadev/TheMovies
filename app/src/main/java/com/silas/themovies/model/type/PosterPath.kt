package com.silas.themovies.model.type

/**
 * Resolution types in poster path mode
 *
 * @param endPoint endPoint for download image
 *
 * @author Silas at 23/02/2020
 */
sealed class PosterPath(val endPoint: String) {
    data class W92(val path: String): PosterPath("w92$path")
    data class W154(val path: String): PosterPath("w154$path")
    data class W185(val path: String): PosterPath("w185$path")
    data class W342(val path: String): PosterPath("w342$path")
    data class W500(val path: String): PosterPath("w500$path")
    data class W780(val path: String): PosterPath("w780$path")
    data class W1280(val path: String): PosterPath("w1280$path")
    data class Original(val path: String): PosterPath("original$path")
}
