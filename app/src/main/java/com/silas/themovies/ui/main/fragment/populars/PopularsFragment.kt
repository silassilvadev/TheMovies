package com.silas.themovies.ui.main.fragment.populars

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.silas.themovies.R
import com.silas.themovies.model.entity.PagedMovies
import com.silas.themovies.ui.LoadingState
import com.silas.themovies.ui.detail.activity.DetailMovieActivity
import com.silas.themovies.ui.generic.GenericFragment
import com.silas.themovies.ui.main.activity.MainActivity
import com.silas.themovies.ui.main.fragment.MoviesAdapter
import com.silas.themovies.utils.custom.PaginationListener
import com.silas.themovies.utils.extensions.addSwipeRefreshRoot
import com.silas.themovies.utils.extensions.hideProgress
import com.silas.themovies.utils.extensions.showProgress
import com.silas.themovies.utils.extensions.startActivity
import kotlinx.android.synthetic.main.fragment_movies.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 * A simple [Fragment] subclass.
 */
class PopularsFragment: GenericFragment(), PopularsContract.View {

    private lateinit var moviesAdapter: MoviesAdapter
    private val popularsPresenter by inject<PopularsPresenter> {
        parametersOf(this)
    }

    private var paginationListener: PaginationListener? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return addSwipeRefreshRoot(R.layout.fragment_movies, { popularsPresenter.loadMovies() })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        popularsPresenter.loadMovies()
    }

    override fun updateMovies(pagedMovies: PagedMovies) {
        this.moviesAdapter.updateMovies(pagedMovies.results)
        setPaginationListener(pagedMovies.totalResults)
    }

    override fun responseError(message: String) = onMessage(message)

    override fun updateLoading(state: LoadingState){
        when (state.name) {
            LoadingState.SHOW.name -> {
                paginationListener?.isLoadingEnable = true
                showProgress()
            }
            LoadingState.HIDE.name -> {
                paginationListener?.isLoadingEnable = false
                hideProgress()
            }
        }
    }

    private fun setUpRecyclerView(){
        this.moviesAdapter = MoviesAdapter { movie ->
            startActivity<DetailMovieActivity>(MainActivity.KEY_MOVIE_SELECTED to movie)
        }
        recycler_view_movies.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler_view_movies.adapter = this.moviesAdapter
    }

    private fun setPaginationListener(totalResults: Int){
        if (this.paginationListener == null) {
            this.paginationListener = PaginationListener(totalResults) {
                this.popularsPresenter.loadMovies(isNextPage = true)
            }.apply {
                recycler_view_movies.addOnScrollListener(this)
            }
        }
    }

    internal fun searchMovies(query: String) = popularsPresenter.loadMovies(query)

}
