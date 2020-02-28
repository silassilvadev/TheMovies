package com.silas.themovies.ui.main.movies

import com.silas.themovies.R

enum class TypeFragment(val position: Int, val title: Int) {
    POPULARS(0, R.string.main_toolbar_title_populars),
    FAVORITES(1, R.string.main_toolbar_title_favorites);

    companion object {
        fun getTypeSelected(position: Int): TypeFragment {
            return if (position == POPULARS.position) POPULARS else FAVORITES
        }
    }
}