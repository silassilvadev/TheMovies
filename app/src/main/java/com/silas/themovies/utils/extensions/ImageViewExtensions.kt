package com.silas.themovies.utils.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.silas.themovies.BuildConfig

fun ImageView.setUpImage(downloadUrl: String, placeholder: Drawable? = null) {
    Glide.with(context)
        .load(BuildConfig.DOWNLOAD_IMAGE_URL + downloadUrl)
        .into(this)
        .waitForLayout()
}