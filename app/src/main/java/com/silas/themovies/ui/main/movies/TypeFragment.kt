package com.silas.themovies.ui.main.movies

import com.silas.themovies.R

/**
 * Type Fragments Selector
 *
 * @param position Fragment or Tab current position
 * @param title Current title
 *
 * @author Silas at 27/02/2020
 */
enum class TypeFragment(val position: Int, val title: Int) {
    POPULARS(0, R.string.main_toolbar_title_populars),
    FAVORITES(1, R.string.main_toolbar_title_favorites);
}