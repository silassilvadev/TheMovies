package com.silas.themovies.ui.main.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.silas.themovies.R
import com.silas.themovies.ui.main.fragment.favorites.FavoritesFragment
import com.silas.themovies.ui.main.fragment.populars.PopularsFragment

/**
 * Fragments adapter with ViewPagers of movies
 *
 * @param fragments List of Fragments to adapt to ViewPagers
 * @param activity Activity used to inflate fragments
 *
 * @author Silas at 27/02/2020
 */
class MainViewPagerAdapter(private val fragments: ArrayList<Fragment>,
                           private val activity: AppCompatActivity):
    FragmentStatePagerAdapter(activity.supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return when (fragments[position]) {
            is PopularsFragment -> activity.getString(R.string.main_toolbar_title_populars)
            is FavoritesFragment -> activity.getString(R.string.main_toolbar_title_favorites)
            else -> activity.getString(R.string.main_toolbar_title)
        }
    }
}