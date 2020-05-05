package com.silas.themovies.model.type

/**
 * Resolution types in back drop mode
 *
 * @param endPoint endPoint for download image
 *
 * @author Silas at 23/02/2020
 */

sealed class BackDrop(val endPoint: String) {
    data class W300(val path: String): BackDrop("w300$path")
    data class W500(val path: String): BackDrop("w500$path")
    data class W780(val path: String): BackDrop("w780$path")
    data class W1280(val path: String): BackDrop("w1280$path")
    data class Original(val path: String): BackDrop("original$path")
}