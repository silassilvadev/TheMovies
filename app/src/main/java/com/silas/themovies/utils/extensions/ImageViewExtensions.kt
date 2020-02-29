package com.silas.themovies.utils.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.silas.themovies.BuildConfig
import com.silas.themovies.R

/**
 * Helper to configure Glide and fetch an image
 * @param downloadUrl Image download url
 * @param placeholder Image waiting while image is not loading
 */
fun ImageView.setUpImage(downloadUrl: String, placeholder: Drawable? = null) {
    Glide.with(context)
        .load(BuildConfig.DOWNLOAD_IMAGE_URL + downloadUrl)
        .placeholder(placeholder)
        .into(this)
        .waitForLayout()
}