package com.silas.themovies.utils.extensions

import androidx.viewpager.widget.ViewPager


/**
 * Abstracts the use of Tab listeners to be able to use only the necessary functions
 *
 * @param onTabReselected
 * @param onTabSelected
 * @param onTabUnselected
 *  High-Order-Functions parameters for selecting only the necessary
 *
 *  @author Silas at 27/02/2020
 */
fun ViewPager.onPageChangeListener(onPageSelected: ((position: Int) -> Unit)? = null,
                                   onPageScrolled: ((position: Int,
                                                     positionOffset: Float,
                                                     positionOffsetPixels: Int) -> Unit)? = null,
                                   onPageScrollStateChanged: ((state: Int) -> Unit)? = null) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) {
            onPageScrollStateChanged?.invoke(state)
        }

        override fun onPageScrolled(position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int) {
            onPageScrolled?.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            onPageSelected?.invoke(position)
        }

    })
}