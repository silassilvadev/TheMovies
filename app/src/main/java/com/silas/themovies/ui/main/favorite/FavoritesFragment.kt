package com.silas.themovies.ui.main.favorite

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.silas.themovies.R
import com.silas.themovies.di.ModulesFactory.FAVORITES_MODULE
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.state.LoadingState
import com.silas.themovies.ui.detail.DetailMovieActivity
import com.silas.themovies.ui.generic.GenericFragment
import com.silas.themovies.ui.main.MainActivity
import com.silas.themovies.ui.main.SearchContract
import com.silas.themovies.ui.main.movies.MoviesAdapter
import com.silas.themovies.utils.extensions.addSwipeRefreshRoot
import com.silas.themovies.utils.extensions.hideProgress
import com.silas.themovies.utils.extensions.showProgress
import com.silas.themovies.utils.extensions.startActivity
import kotlinx.android.synthetic.main.fragment_movies.*
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named


/**
 * @author Silas at 26/02/2020
 */
class FavoritesFragment:
    GenericFragment(),
    FavoritesContract.View,
    SearchContract {

    companion object {
        private const val BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout"
    }

    private val favoritesScope = getKoin().getOrCreateScope("favorites", named(FAVORITES_MODULE))
    private val favoritesPresenter by favoritesScope.inject<FavoritesPresenter> {
        parametersOf(this)
    }
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return addSwipeRefreshRoot(R.layout.fragment_movies, { favoritesPresenter.loadFavorites() })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.apply {
            getParcelable<Parcelable>(BUNDLE_RECYCLER_LAYOUT)
            recycler_view_movies.layoutManager?.onRestoreInstanceState(this)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(
            BUNDLE_RECYCLER_LAYOUT,
            recycler_view_movies.layoutManager?.onSaveInstanceState()
        )
    }

    override fun onResume() {
        super.onResume()
        favoritesPresenter.loadFavorites()
    }

    override fun onDestroy() {
        super.onDestroy()
        favoritesScope.close()
    }

    private fun setUpRecyclerView(){
        this.moviesAdapter =
            MoviesAdapter { movie ->
                startActivity<DetailMovieActivity>(MainActivity.KEY_MOVIE_SELECTED to movie)
            }

        recycler_view_movies.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = this@FavoritesFragment.moviesAdapter
        }
    }

    override fun updateFavorites(movies: ArrayList<Movie>) {
        this.moviesAdapter.updateMovies(movies)
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
