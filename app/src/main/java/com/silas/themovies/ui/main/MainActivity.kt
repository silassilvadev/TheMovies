package com.silas.themovies.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.silas.themovies.R
import com.silas.themovies.ui.generic.GenericActivity
import com.silas.themovies.ui.main.movies.MoviesFragment
import com.silas.themovies.ui.main.movies.TypeFragment
import com.silas.themovies.utils.extensions.onTabSelected
import kotlinx.android.synthetic.main.activity_main_movies.*

/**
 * Responsible for providing instances of Fragments and their ViewPagers,
 * and integrating them with Tabs to show lists with similar items
 *
 * @property viewPagerAdapter Used to adapt Fragments in ViewPager
 * @property currentQuery Current search entered by the user. When it is empty,
 * all the films on the first page will be loaded
 *
 * @author Silas at 22/02/2020
 */
class MainActivity : GenericActivity(), SearchView.OnQueryTextListener, SearchView.OnCloseListener  {

    companion object {
        const val KEY_MOVIE = "keyMovieId"
    }

    private lateinit var viewPagerAdapter: MainViewPagerAdapter
    internal var currentQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_movies)
        setUpCustomToolbar(toolbar_main, getString(R.string.main_toolbar_title), false)
        initView()
        initListeners()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { itQuery ->
            currentQuery = itQuery
            loadInCurrentFragment()
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        onQueryTextSubmit(newText)
        return false
    }

    override fun onClose(): Boolean {
        onQueryTextSubmit("")
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.main_toolbar, it)
            val itemSearch = it.findItem(R.id.item_toolbar_search)
            val searchView = itemSearch.actionView as SearchView

            searchView.setOnQueryTextListener(this)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun initView() {
        this.viewPagerAdapter = MainViewPagerAdapter(
            arrayListOf(
                MoviesFragment(TypeFragment.POPULARS),
                MoviesFragment(TypeFragment.FAVORITES)),
            this).apply {
            view_pager_main_container.adapter = this
        }
        tab_layout_main.setupWithViewPager(view_pager_main_container, false)
    }

    private fun initListeners(){
        tab_layout_main.onTabSelected {
            loadInCurrentFragment()
        }
    }

    /**
     * Responsible for loading the current Snippet by sending your current search
     */
    private fun loadInCurrentFragment() {
        (this.viewPagerAdapter.getItem(tab_layout_main.selectedTabPosition) as MoviesFragment).apply {
            loadMovies(currentQuery)
        }
    }
}
