package com.silas.themovies.ui.main.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.silas.themovies.R
import com.silas.themovies.ui.generic.GenericActivity
import com.silas.themovies.ui.main.fragment.favorites.FavoritesFragment
import com.silas.themovies.ui.main.fragment.populars.PopularsFragment
import com.silas.themovies.utils.extensions.onPageChangeListener
import com.silas.themovies.utils.extensions.setupAllAnimations
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.activity_main_movies.*

/**
 * Responsible for providing instances of Fragments and their ViewPagers,
 * and integrating them with Tabs to show lists with similar items
 *
 * @property viewPagerAdapter Used to adapt Fragments in ViewPager
 *
 * @author Silas at 22/02/2020
 */
class MainActivity : GenericActivity(), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener  {

    companion object {
        const val KEY_MOVIE_SELECTED = "keyMovieId"
    }

    private lateinit var itemSearch: MenuItem
    private lateinit var searchView: SearchView
    private lateinit var viewPagerAdapter: MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_movies)
        setUpCustomToolbar(toolbar_main, getString(R.string.main_toolbar_title), false)
        initViews()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { itQuery ->
            loadInCurrentFragment(itQuery)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        onQueryTextSubmit(newText)
        return false
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        tab_layout_main.isVisible = false
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        tab_layout_main.isVisible = true
        onQueryTextChange("")
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.main_toolbar, it)
            itemSearch = it.findItem(R.id.item_toolbar_search).apply {
                title = getSearchTitle()
            }
            searchView = itemSearch.actionView as SearchView
            setUpListeners()
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun initViews() {
        this.viewPagerAdapter = MainViewPagerAdapter(
            arrayListOf(PopularsFragment(), FavoritesFragment()), this
        ).apply {
            view_pager_main_container.adapter = this
        }

        coordinator_layout_main.setupAllAnimations(true)
        tab_layout_main.setupWithViewPager(view_pager_main_container, false)
    }

    private fun setUpListeners(){
        searchView.setOnQueryTextListener(this)
        itemSearch.setOnActionExpandListener(this)
        view_pager_main_container.onPageChangeListener {
            itemSearch.title = getSearchTitle(it)
        }
    }

    /**
     * Responsible for loading the current Snippet by sending your current search
     */
    private fun loadInCurrentFragment(query: String) {
        when (val fragment = this.viewPagerAdapter.getItem(tab_layout_main.selectedTabPosition)) {
            is FavoritesFragment -> fragment.searchFavorites(query)
            is PopularsFragment -> fragment.searchMovies(query)
        }
    }

    private fun getSearchTitle(position: Int = 0): String {
        return when (position) {
            1 -> getString(R.string.main_toolbar_search_favorites_hint)
            else -> getString(R.string.main_toolbar_search_movies_hint)
        }
    }
}