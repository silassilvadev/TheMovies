package com.silas.themovies.ui.main

import com.silas.themovies.R

/**
 * Type Fragments Selector
 *
 * @param title Current title
 *
 * @author Silas at 27/02/2020
 */
enum class TypeFragment(val title: Int) {
    POPULARS(R.string.main_toolbar_title_populars),
    FAVORITES(R.string.main_toolbar_title_favorites);
}