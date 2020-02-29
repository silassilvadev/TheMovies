package com.silas.themovies.utils.extensions

/**
 * Remove special characters from the String
 * @return Returns the clean String
 */
fun String.simpleMaskDate() = this.replace("[\\s]|[\\W]".toRegex(), "/")

/**
 * Converts a String that is convertible to Int in percentage of type Double
 * @return Returns the percentage in Double converted to String
 */
fun String.convertToPercent() = ((this.toDouble() * 100)/10).toString()
