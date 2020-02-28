package com.silas.themovies.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.silas.themovies.TheMoviesApplication.Companion.application

fun Context.myGetColor(colorId: Int): Int = ContextCompat.getColor(this, colorId)

fun Context.myGetDrawable(drawableId: Int): Drawable? = ContextCompat.getDrawable(this, drawableId)
