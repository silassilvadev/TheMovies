package com.silas.themovies.utils.extensions

import android.animation.LayoutTransition
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import com.silas.themovies.R

/**
 * Configures Parent and/or Child Views to animate your changes
 *
 * @param isAnimateParent Specifies whether the Parent View should be animated by changing the Children
 * @param isAnimate Specifies whether the View should animate your changes
 *
 * @author Silas at 23/02/2020
 */
fun ViewGroup.setupAllAnimations(isAnimateParent: Boolean = false, isAnimate: Boolean = true) {
    LayoutTransition().apply {
        setAnimateParentHierarchy(isAnimateParent)
        if (isAnimate) enableTransitionType(LayoutTransition.CHANGING)
        else disableTransitionType(LayoutTransition.CHANGING)
        this@setupAllAnimations.layoutTransition = this
    }
}

/**
 * Animates the extended view in the form of fading
 *
 * @param isFadeIn Enable Fade animation
 * @param delay Animation waiting time
 * @param onFinishAnimation Function that must be executed when ending animation
 *
 * @author Silas at 23/02/2020
 */
fun View.animateFade(isFadeIn: Boolean = true, delay: Long = 0, onFinishAnimation: ((isFinish: Boolean) -> Unit)? = null){
    this.context?.let {
        val fadeIn = AnimationUtils.loadAnimation(it, R.anim.alpha_in)
        val fadeOut = AnimationUtils.loadAnimation(it, R.anim.alpha_out)
        fadeIn.reset()
        fadeOut.reset()
        this.startAnimation(if (isFadeIn) fadeIn else fadeOut)
        this.postDelayed({
            this.isVisible = isFadeIn
            Handler().postDelayed({
                onFinishAnimation?.invoke(true)
            }, delay)
        }, if (isFadeIn) fadeIn.duration else fadeOut.duration)
    }
}
