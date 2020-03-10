package com.silas.themovies.ui.main.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.silas.themovies.R
import com.silas.themovies.model.dto.response.PagedListMovies
import com.silas.themovies.ui.detail.DetailMovieActivity
import com.silas.themovies.ui.generic.GenericFragment
import com.silas.themovies.ui.main.MainActivity
import com.silas.themovies.utils.extensions.*
import kotlinx.android.synthetic.main.fragment_movies.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Dynamic filling class, responsible for showing a list of films to the user,
 * sometimes popular, sometimes favorites.
 *
 * @param typeFragment Fragment identifier, which can be of the types:
 *  [TypeFragment.POPULARS] and [TypeFragment.FAVORITES]
 * @property moviesViewModel Instance of our business layer, responsible for searching the data
 * @property moviesAdapter Used to adapt RecyclerView data and respond to your changes
 * @property pagedListMovies Contains the page, the total pages, the total films and the updated films
 * @property currentQuery Current search entered by the user. When it is empty,
 * all the films on the first page will be loaded
 * @property currentPage Current page viewed by the user, or that will be loaded soon
 *
 * @author Silas at 26/02/2020
 */
class MoviesFragment(internal val typeFragment: TypeFragment): GenericFragment() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val moviesViewModel by viewModel<MoviesViewModel> {
        parametersOf(this)
    }
    private lateinit var moviesAdapter: MovieAdapter
    private lateinit var pagedListMovies: PagedListMovies

    private var currentQuery = ""
    private var currentPage = 1
    private var currentScrollPosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.swipeRefreshLayout = addSwipeRefreshRoot(R.layout.fragment_movies, {
            loadMovies()
        }) as SwipeRefreshLayout
        return this.swipeRefreshLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    /**
     * Due to the constant updating of the API and also the favorites,
     * the App always updates onResume to always load new changes
     */
    override fun onResume() {
        super.onResume()
        (requireActivity() as? MainActivity)?.apply {
            loadMovies(currentQuery, currentPage)
        }
    }

    /**
     * Acts as a Fragment type classifier ([TypeFragment.POPULARS] or [TypeFragment.FAVORITES]),
     * so that the correct search function is called
     *
     * @param query Text to be searched
     * @param page Next page to be searched
     */
    internal fun loadMovies(query: String = "", page: Int = 1) {
        currentQuery = query
        currentPage = page
        if (typeFragment == TypeFragment.POPULARS) getPopulars() else getFavorites()
    }

    private fun initListeners(){
        this.swipeRefreshLayout.setOnRefreshListener {
            loadMovies()
            this.swipeRefreshLayout.isRefreshing = false
        }

        // Listener was used to capture the moment when the user comes close to the end of the list,
        // and thus load the next page of films
        recycler_view_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                (recyclerView.layoutManager as? GridLayoutManager)?.apply {

                    // List update function is only enabled when it is at the top
                    this@MoviesFragment.swipeRefreshLayout.isEnabled = findFirstVisibleItemPosition() <= 1

                    // Pagination of the list when necessary
                    if (isPaginationNecessary(newState, pagedListMovies)) {
                        loadMovies(currentQuery, currentPage.plus(1))
                    }
                }
            }
        })
    }

    /**
     * Request a list of popular movies for ViewModel
     */
    private fun getPopulars() {
        if (currentPage == 1 && currentQuery.isBlank()) showProgress()
        moviesViewModel.getPopulars(currentPage, currentQuery).observe(viewLifecycleOwner, Observer {
            hideProgress()
            if (currentPage > 1) {
                this.pagedListMovies.updatePage(it)
                this.moviesAdapter.notifyDataSetChanged()
            } else {
                this.pagedListMovies = it
                setUpRecyclerView()
            }
            if (it.results.isEmpty()) onResponseError(getString(R.string.error_empty_list_movies))
        })
    }

    /**
     * Request a list of favorite movies for ViewModel
     */
    private fun getFavorites() {
        showProgress()
        moviesViewModel.getFavorites(currentQuery).observe(viewLifecycleOwner, Observer {
            hideProgress()
            pagedListMovies = it
            setUpRecyclerView()
            if (pagedListMovies.results.isEmpty()) onResponseError(getString(R.string.error_empty_favorite_movies))
        })
    }

    /**
     * Configures recyclerview, a LayoutManager for handling layout changes,
     * and an adapter for handling changes to list items
     *
     * [currentScrollPosition] Clicked position saved, to ensure that when returning to the screen
     * the user will scroll to the previous position
     */
    private fun setUpRecyclerView() {
        recycler_view_movies.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            moviesAdapter = MovieAdapter(pagedListMovies.results) { position, movie ->
                currentScrollPosition = position - 1
                startActivity<DetailMovieActivity>(MainActivity.KEY_MOVIE to movie)
            }
            adapter = moviesAdapter
            scrollToPosition(currentScrollPosition)
        }
    }
}
