package com.silas.themovies.utils.extensions

fun List<*>.convertInTextList(): String {
    var textList = ""
    for ((position, item) in this.withIndex()) {
        textList += if (position < this.size - 1) "${item.toString()}," else " e ${item.toString()}"
    }
    return textList
}