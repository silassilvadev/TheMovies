package com.silas.themovies.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.silas.themovies.ui.main.movies.MoviesFragment

/**
 * Fragments adapter with ViewPagers of movies
 *
 * @param fragments List of Fragments to adapt to ViewPagers
 * @param activity Activity used to inflate fragments
 *
 * @author Silas at 27/02/2020
 */
class MainViewPagerAdapter(private val fragments: ArrayList<MoviesFragment>,
                           private val activity: AppCompatActivity)
    : FragmentStatePagerAdapter(activity.supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return activity.getString(fragments[position].typeFragment.title)
    }
}