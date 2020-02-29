package com.silas.themovies.model.entity.type

/**
 * Resolution types in back drop mode
 *
 * @param resolution Resolution selected
 *
 * @author Silas at 23/02/2020
 */
enum class BackDropType(val resolution: String) {
    W_300("w300"),
    W_500("w500"),
    W_780("w780"),
    W_1280("w1280"),
    W_ORIGINAL("original");
}