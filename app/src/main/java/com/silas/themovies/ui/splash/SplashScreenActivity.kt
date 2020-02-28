package com.silas.themovies.ui.splash

import android.os.Bundle
import androidx.core.view.isVisible
import com.silas.themovies.R
import com.silas.themovies.ui.generic.GenericActivity
import com.silas.themovies.ui.main.MainActivity
import com.silas.themovies.utils.extensions.animateFade
import com.silas.themovies.utils.extensions.startActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : GenericActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        lottie_animation_view_splash.playAnimation()
        lottie_animation_view_splash.animateFade(true, 1800) { isFinishAnimationLayout ->
            if (isFinishAnimationLayout){
                lottie_animation_view_splash.animateFade(false, 200) {
                    lottie_animation_view_splash.isVisible = false
                    startActivity<MainActivity>()
                    finish()
                }
            }
        }
    }
}
