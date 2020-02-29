package com.silas.themovies.model.entity.type
/**
 * Resolution types in poster path mode
 *
 * @param resolution Resolution selected
 *
 * @author Silas at 23/02/2020
 */
enum class PosterPathType(val resolution: String) {
    W_92("w92"),
    W_154("w154"),
    W_185("w185"),
    W_342("w342"),
    W_500("w500"),
    W_780("w780"),
    W_ORIGINAL("original");
}
