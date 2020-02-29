package com.silas.themovies.utils.extensions

import com.silas.themovies.model.dto.response.Genre

/**
 * Converts a list to text
 * @return List converted to text
 */
fun List<Genre>.convertInTextList(): String {
    var textList = ""
    for ((position, item) in this.withIndex()) {
        textList += if (position < this.size - 1) "${item.name}, " else " e ${item.name}"
    }
    return textList
}