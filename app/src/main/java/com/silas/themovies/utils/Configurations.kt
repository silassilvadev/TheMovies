package com.silas.themovies.utils

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat

fun getSystemLanguage(): String {
    return ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0].toLanguageTag()
}