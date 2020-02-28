package com.silas.themovies.utils.extensions

import com.google.android.material.tabs.TabLayout

fun TabLayout.onTabSelected(onTabReselected: ((tab: TabLayout.Tab?) -> Unit)? = null,
                            onTabUnselected: ((tab: TabLayout.Tab?) -> Unit)? = null,
                            onTabSelected: ((tab: TabLayout.Tab?) -> Unit)? = null) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
        override fun onTabReselected(tab: TabLayout.Tab?) {
            onTabReselected?.invoke(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            onTabUnselected?.invoke(tab)
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            onTabSelected?.invoke(tab)
        }

    })
}