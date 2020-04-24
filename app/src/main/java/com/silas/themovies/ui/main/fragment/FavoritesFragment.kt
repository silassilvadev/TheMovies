package com.silas.themovies.ui.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.silas.themovies.R
import com.silas.themovies.di.ModulesFactory.FAVORITES_MODULE
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.ui.LoadingState
import com.silas.themovies.ui.detail.activity.DetailMovieActivity
import com.silas.themovies.ui.generic.GenericFragment
import com.silas.themovies.ui.main.activity.SearchContract
import com.silas.themovies.ui.main.activity.MainActivity
import com.silas.themovies.ui.main.presenter.favorites.FavoritesContract
import com.silas.themovies.ui.main.presenter.favorites.FavoritesPresenter
import com.silas.themovies.utils.extensions.*
import kotlinx.android.synthetic.main.fragment_movies.*
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

/**
 * @author Silas at 26/02/2020
 */
class FavoritesFragment: GenericFragment(), FavoritesContract.View, SearchContract {

    private lateinit var moviesAdapter: MoviesAdapter
    private var layoutManager: GridLayoutManager? = null

    private val favoritesScope = getKoin().getOrCreateScope("favorites", named(FAVORITES_MODULE))
    private val favoritesPresenter by favoritesScope.inject<FavoritesPresenter> {
        parametersOf(this)
    }

    private var currentScrollPosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return addSwipeRefreshRoot(R.layout.fragment_movies, { favoritesPresenter.loadFavorites() })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        favoritesPresenter.loadFavorites()
    }

    override fun onPause() {
        super.onPause()
        currentScrollPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
    }

    override fun onDestroy() {
        super.onDestroy()
        favoritesScope.close()
    }

    private fun setUpRecyclerView(){
        this.moviesAdapter = MoviesAdapter { movie ->
            startActivity<DetailMovieActivity>(MainActivity.KEY_MOVIE_SELECTED to movie)
        }

        recycler_view_movies.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = this@FavoritesFragment.moviesAdapter
        }
    }

    private fun updateScrollList() {
        layoutManager?.scrollToPosition(this@FavoritesFragment.currentScrollPosition)
    }

    override fun updateFavorites(movies: ArrayList<Movie>) {
        this.moviesAdapter.updateMovies(movies)
        updateScrollList()
    }

    override fun responseError(message: String) {
        onMessage(message)
    }

    override fun updateLoading(state: LoadingState) {
        when (state) {
            LoadingState.Show -> showProgress()
            LoadingState.Hide -> hideProgress()
        }
    }

    override fun searchMovies(query: String) = favoritesPresenter.loadFavorites(query)

}
