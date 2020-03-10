package com.silas.themovies.utils.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

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
