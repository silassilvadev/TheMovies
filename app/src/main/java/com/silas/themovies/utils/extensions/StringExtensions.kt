package com.silas.themovies.utils.extensions

fun String.simpleMaskDate() = this.replace("[\\s]|[\\W]".toRegex(), "/")

fun String.convertToPercent() = ((this.toDouble() * 100)/10).toString()
