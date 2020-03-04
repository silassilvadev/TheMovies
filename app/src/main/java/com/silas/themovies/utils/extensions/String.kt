package com.silas.themovies.utils.extensions

import android.util.Log
import java.lang.Exception
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val LANGUAGE_PT = "pt"
const val COUNTRY_BR = "BR"
const val MASK_DATE_BRAZIL = "dd/MM/yyyy"

/**
 * Remove special characters from the String
 * @return Returns the clean String
 */
fun String.formatDate(mask: String = MASK_DATE_BRAZIL): String {
    return try {
        val locale = Locale(LANGUAGE_PT, COUNTRY_BR)
        val currentDate = Date()
        val formatter = SimpleDateFormat(mask, locale)
        formatter.isLenient = false
        formatter.parse(this)?.let {
            if (it.time > currentDate.time) ""
            else formatter.format(it)
        } ?: ""
    } catch (exception: Exception) {
        ""
    }
}
