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

/**
 * Helper for color capture
 * @param colorId Resource id of color a capture
 */
fun Context.myGetColor(colorId: Int): Int = ContextCompat.getColor(this, colorId)

/**
 * Helper for drawable capture
 * @param drawableId Resource id of drawable a capture
 */
fun Context.myGetDrawable(drawableId: Int): Drawable? = ContextCompat.getDrawable(this, drawableId)
