package com.silas.themovies.utils.extensions

import java.text.SimpleDateFormat
import java.util.*

const val LANGUAGE_PT = "BR"
const val COUNTRY_BR = "pt"
const val DATE_BRAZIL = "dd/MM/yyyy"

/**
 * Converts milliseconds to an hour String
 * @param mask Mask to be used for conversion
 * @return Date converted to String
 */
fun Long.convertInDateString(mask: String = DATE_BRAZIL): String {
    val locale = Locale(
        LANGUAGE_PT,
        COUNTRY_BR
    )
    val formatter = SimpleDateFormat(mask, locale)
    val date = Date(this)
    return formatter.format(date)
}