package com.silas.themovies.utils.extensions

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.silas.themovies.BuildConfig
import java.lang.Exception

/**
 * Helper to configure Glide and fetch an image
 * @param downloadUrl Image download url
 * @param placeholder Image waiting while image is not loading
 */
fun ImageView.setUpImage(downloadUrl: String, placeholder: Drawable? = null) {
    try {
        Glide.with(this)
            .load(BuildConfig.DOWNLOAD_IMAGE_URL + downloadUrl)
            .placeholder(placeholder)
            .into(this@setUpImage)
            .waitForLayout()
    } catch (exception: Exception){
        exception.printStackTrace()
        Log.i("error", "Error downloading or opening image")
    }
}