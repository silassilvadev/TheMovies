package com.silas.themovies.ui.main

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.silas.themovies.R
import com.silas.themovies.ui.generic.GenericActivity
import com.silas.themovies.ui.main.movies.MoviesFragment
import com.silas.themovies.ui.main.movies.TypeFragment
import com.silas.themovies.utils.extensions.onTabSelected
import kotlinx.android.synthetic.main.activity_main_movies.*

class MainActivity : GenericActivity(), SearchView.OnQueryTextListener, SearchView.OnCloseListener  {

    companion object {
        const val KEY_MOVIE = "keyMovieId"
    }

    private lateinit var adapter: MainViewPagerAdapter
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
        loadInCurrentFragment()
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
        this.adapter = MainViewPagerAdapter(
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

    private fun loadInCurrentFragment(page: Int = 1){
        (this.adapter.getItem(tab_layout_main.selectedTabPosition) as MoviesFragment).apply {
            loadMovies(currentQuery, page)
        }
    }
}
